
import banking.*;
import java.sql.*;
import java.util.*;

public class Main {

    //public static Map<Integer,Long> accounts = new HashMap<>();

    public static void main(String[] args) {

        DB.createDB(args[1]);
        DB.createTable();
        String menu= """
                1. Create an account
                2. Log into account
                0. Exit
                """;

        String subMenu= """
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """;

        Scanner scan = new Scanner(System.in);
        int opc = 5;
        while(opc!=0){
            System.out.print(menu);
            opc=scan.nextInt();
            switch (opc){
                case 1:
                    long newNumber = User.generateIdentifier();
                    int newPin = User.generatePIN();
                    //accounts.put(newPin,newNumber);
                    DB.insertCard(Long.toString(newNumber),Integer.toString(newPin));

                    System.out.printf("""
                        
                        Your card has been created
                        Your card number:
                        %d
                        Your card PIN:
                        %d
                        
                        """,newNumber,newPin);
                    break;

                case 2:
                    System.out.println("Enter your card number:");
                    long cardInput = scan.nextLong();
                    System.out.println("Enter your PIN:");
                    int pinInput = scan.nextInt();
                    boolean logged = DB.verifyLogin(pinInput,cardInput);

                    if (logged){
                        System.out.println("You have successfully logged in!");
                    }else{
                        System.out.println("Wrong card number or PIN!");
                    }
                    while (logged){
                        User currentUser = new User(cardInput,pinInput);
                        System.out.println(subMenu);
                        int opcSubMenu=scan.nextInt();
                        switch (opcSubMenu){
                            case 1:
                                System.out.println("Balance: "+DB.checkBalance(currentUser.getPin()));
                                break;
                            case 2:
                                System.out.println("Enter income: ");
                                int income = scan.nextInt();
                                DB.addIncome(currentUser.getPin(),income);
                                System.out.println("Income was added!");
                                break;
                            case 3:
                                System.out.println("Transfer \nEnter card number: ");
                                Long destinatary = scan.nextLong();
                                if(User.cardIsValid(destinatary)){
                                    if(DB.cardExists(destinatary)){
                                        System.out.println("Enter how much money you want to transfer: ");
                                        int transferAmount = scan.nextInt();
                                        if(DB.checkBalance(currentUser.getPin())>=transferAmount){
                                            DB.transfer(transferAmount,currentUser.getCardNumber(),destinatary);
                                            System.out.println("Succes!");
                                        }else{
                                            System.out.println("Not enough money!");
                                        }
                                    }else{
                                        System.out.println("Such a card does no exist.");
                                    }
                                }else{
                                    System.out.println("Probably you made a mistake in the card number." +
                                            "Please try again!");
                                }
                                break;
                            case 4:
                                DB.closeAccount(currentUser.getPin());
                                System.out.println("The account has been closed!");
                                logged=false;
                                break;
                            case 5:
                                logged=false;
                                break;
                            case 0:
                                opc=0;
                                System.exit(0);
                                break;
                        }
                    }
                    break;
            }
        }
        System.out.println("Bye!");

    }


}
