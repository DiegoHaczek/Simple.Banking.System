package banking;

import java.util.Random;

public class User {

        private long cardNumber;
        private int pin;

        public User(long cardNumber, int pin){
            this.cardNumber=cardNumber;
            this.pin=pin;
        }
        public long getCardNumber(){
            return this.cardNumber;
        }

        public int getPin()
        {
            return this.pin;
        }
        public static long generateIdentifier() {
        String newNumber;
        Random rand = new Random();
        int identifier1= rand.nextInt(99999-10000) + 10000;
        int identifier2= rand.nextInt(9999-1000) + 1000;

        newNumber="400000"+String.valueOf(identifier1)+String.valueOf(identifier2);
        char[] tempArray=newNumber.toCharArray();
        //System.out.println(newNumber);
        int[] cardArray= new int[tempArray.length+1];

        for(int i = 0 ; i<tempArray.length ; i++){
            cardArray[i]=Character.getNumericValue(tempArray[i]);
        }

        int control = 0;
        //multiply odd position by 2
        for (int i = 0; i<tempArray.length ; i++){
            if (i%2==0){
                cardArray[i]=cardArray[i]*2;
            }
            //subtract -9 to n>9
            if(cardArray[i]>9){
                cardArray[i]-=9;
            }
            control+=cardArray[i];
        }

        int checksum = 0;
        for (int i = 0; i<10 ; i++){

            if((control+i)%10==0) {
                checksum=i;
                break;
            }
        }
        //System.out.printf("\nControl: {%d} CheckSum: {%d}\n",control,checksum);

        return Long.parseLong(newNumber+checksum);
    }
        public static int generatePIN(){
            Random rand = new Random();
            int pin = rand.nextInt(9999-1000) + 1000;
            return pin;
        }

        public static boolean cardIsValid(long cardNumber){
            String cardNo = Long.toString(cardNumber);
            int nDigits = cardNo.length();
            int nSum = 0;
            boolean isSecond = false;
            for (int i = nDigits - 1; i >= 0; i--)
            {
                int d = cardNo.charAt(i) - '0';

                if (isSecond == true)
                    d = d * 2;
                nSum += d / 10;
                nSum += d % 10;

                isSecond = !isSecond;
            }
            return (nSum % 10 == 0);
        }

}
