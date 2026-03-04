package combis.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class TokenUtil {

    private static final String SECRET = "CHANGE_ME_SECRET";
    private static final long EXPIRES_SECONDS = 3600;

    public static String generateToken(Long userId, String pseudo,String role) {
        long exp = Instant.now().getEpochSecond() + EXPIRES_SECONDS;
        String payload = userId + "|" + pseudo + "|" + exp;

        String payloadB64 = b64url(payload);
        String sig = hmac(payloadB64, SECRET);

        return payloadB64 + "." + sig;
    }

    public static Long verifyAndGetUserId(String token) {
        if (token == null || !token.contains(".")) return null;

        String[] parts = token.split("\\.", 2);
        String payloadB64 = parts[0];
        String sig = parts[1];

        String expected = hmac(payloadB64, SECRET);
        if (!constantTimeEquals(sig, expected)) return null;

        String payload = fromB64url(payloadB64);
        String[] items = payload.split("\\|");
        if (items.length != 3) return null;

        Long userId = Long.parseLong(items[0]);
        long exp = Long.parseLong(items[2]);

        if (Instant.now().getEpochSecond() > exp) return null;
        return userId;
    }

    private static String b64url(String s) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String fromB64url(String b64) {
        return new String(Base64.getUrlDecoder().decode(b64), StandardCharsets.UTF_8);
    }

    private static String hmac(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException("Erreur HMAC", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] x = a.getBytes(StandardCharsets.UTF_8);
        byte[] y = b.getBytes(StandardCharsets.UTF_8);
        if (x.length != y.length) return false;
        int r = 0;
        for (int i = 0; i < x.length; i++) r |= (x[i] ^ y[i]);
        return r == 0;
    }
}
