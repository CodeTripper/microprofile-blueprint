package in.codetripper.blueprint.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;

public class JwtToken {
	public static String getToken() {
		PrivateKey key;
		String jwt = "";
		try {
			key = readPrivateKey();
			jwt = generateJWT(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jwt;
	}

	private static String generateJWT(PrivateKey key) {
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).keyID("theKeyId").build();

		MPJWTToken token = new MPJWTToken();
		token.setExp(System.currentTimeMillis() + 3000000); // 30 days expiration!
		token.setGroups(Arrays.asList("user", "admin"));
		token.setSub("John");
		token.setUpn("John");
		token.setIss("https://auth.example.com"); // Must match the expected issues configuration values
		token.setAud("gatewayservice");
		token.setJti(UUID.randomUUID().toString());
		token.setIat(System.currentTimeMillis());
		token.addAdditionalClaims("custom-value", "John specific value");
		JWSObject jwsObject = new JWSObject(header, new Payload(token.toJSONString()));
		JWSSigner signer = new RSASSASigner(key);
		try {
			jwsObject.sign(signer);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		return jwsObject.serialize();
	}

	private static PrivateKey readPrivateKey() throws IOException {
		InputStream inputStream = JwtToken.class.getResourceAsStream("/privateKey.pem");
		PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream));
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(new BouncyCastleProvider());
		Object object = pemParser.readObject();
		KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
		return kp.getPrivate();
	}

}
