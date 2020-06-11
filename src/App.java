import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Formatter;

public class App {
    private static final String HMAC_SHA256 = "HmacSHA256";

    public static void main(String[] args) throws IOException {
        if (args.length % 2 != 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            SecureRandom random = new SecureRandom();

            String key = getRandomHexString();
            int aiStep =  random.nextInt(args.length);
            String hmac = getHmac(key, args[aiStep]);

            System.out.println("HMAC: " + hmac);
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + 1 + " - " + args[i]);
            }
            System.out.println("0 - exit");
            System.out.print("Enter you move: ");
            int myStep = getMyStep(args, reader);
            System.out.print("You move: ");
            System.out.println(args[myStep]);

            System.out.print("Computer move: ");
            System.out.println(args[aiStep]);
            gameProcess(aiStep, myStep, args);
            System.out.println("HMAC key: " + key);
        } else {
            System.out.println("Incorrect count variable");
        }
    }

    private static int getMyStep(String[] args, BufferedReader reader) throws IOException {
        int myStep = Integer.parseInt(reader.readLine());
        if (myStep == 0) {
            System.exit(0);
        }else if (myStep < 0 || myStep > args.length) {
            System.out.println("Please enter variable between 1 and " + args.length);
            getMyStep(args, reader);
        }
        return myStep - 1;
    }

    private static void gameProcess(int aiStep, int myStep, String[] args) {
        String answer = "";
        if (myStep == aiStep) {
            System.out.println("Draw");
        } else if (myStep > aiStep) {
            answer = (myStep - aiStep > args.length / 2) ? "You lose" : "You win";
        } else {
            answer = (aiStep - myStep > args.length / 2) ? "You win" : "You lose";
        }
        System.out.println(answer);
    }

    private static String getRandomHexString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 64) {
            sb.append(Integer.toHexString(random.nextInt()));
        }
        return sb.toString().substring(0, 64);
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private static String getHmac(String key, String data) {
        String result = "";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }
}
