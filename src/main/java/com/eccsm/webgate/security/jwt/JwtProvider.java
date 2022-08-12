package com.eccsm.webgate.security.jwt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.eccsm.webgate.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider implements IJwtProvider {

	@Value("${authentication.jwt.expiration-in-ms}")
	private Long JWT_EXPIRATION_IN_MS;

	private static final String JWT_TOKEN_PREFIX = "Bearer";
	private static final String JWT_TOKEN_STRING = "Authorization";

	private final PrivateKey jwtPrivateKey;
	private final PublicKey jwtPublicKey;

	public JwtProvider(@Value("${authentication.jwt.public-key}") String jwtPublicKeyStr,
			@Value("${authentication.jwt.private-key}") String jwtPrivateKeyStr) {

		try {
			KeyFactory keyFactory = getKeyFactory();
			Base64.Decoder decoder = Base64.getDecoder();
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(jwtPrivateKeyStr.getBytes()));
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(jwtPublicKeyStr.getBytes()));
			jwtPrivateKey = keyFactory.generatePrivate(privateKeySpec);
			jwtPublicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
			throw new RuntimeException("Invalid Key Specification");
		}

	}

	@Override
	public String generateToken(UserPrincipal authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining());

		return Jwts.builder().setSubject(authentication.getUsername()).claim("userId", authentication.getId())
				.claim("roles", authorities).setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
				.signWith(jwtPrivateKey, SignatureAlgorithm.RS512).compact();

	}

	@Override
	public Authentication getAuthentication(HttpServletRequest request) {
		Claims claims = getClaims(request);

		if (claims == null)
			return null;

		String username = claims.getSubject();
		Long userId = claims.get("userId", Long.class);
		List<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UserDetails userDetails = new UserPrincipal(userId, username, null);
		return username != null ? new UsernamePasswordAuthenticationToken(userDetails, null, authorities) : null;

	}

	@Override
	public boolean validateToken(HttpServletRequest request) {
		Claims claims = getClaims(request);

		if (claims == null)
			return false;

		if (claims.getExpiration().before(new Date()))
			return false;

		return true;
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JWT_TOKEN_STRING);
		if (bearerToken != null && !bearerToken.isBlank() && bearerToken.startsWith(JWT_TOKEN_PREFIX))
			return bearerToken.substring(7);
		return null;

	}

	private KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {

		return KeyFactory.getInstance("RSA");

	}

	private Claims getClaims(HttpServletRequest request) {
		String token = resolveToken(request);
		if (token == null || token.isBlank())
			return null;

		return Jwts.parserBuilder().setSigningKey(jwtPublicKey).build().parseClaimsJws(token).getBody();
	}

}
