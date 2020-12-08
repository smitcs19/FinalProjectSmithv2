package com.company;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

class stakeholder {
    private String id;
    private String name;
    private String address;
    private double balance;

    public stakeholder(){
        this.id = "none";
        this.name = "none";
        this.address = "none";
        this.balance = 0;
    }

    public stakeholder(String id, String name, String address, double balance){
        this.id=id;
        this.name=name;
        this.address=address;
        this.balance=balance;
    }

    public void setId(String id) {this.id=id;}
    public void setName(String name) {this.name=name;}
    public void setAddress(String address) {this.address=address;}
    public void setBalance(double balance) {this.balance=balance;}

    public String getId() {return id;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public double getBalance() {return balance;}

    @Override
    public String toString(){
        String ret = "ID: " + id +"; ";
        ret+= "Name: " + name + "; ";
        ret+= "Address: " + address + "; ";
        ret+= "Balance: " + balance + ".";
        return ret;
    }
}

class artefact {
    public String id;
    public String name;
    public String countryOfOrigin;
    public stakeholder currentOwner;

    public artefact(){
        this.id="none";
        this.name="none";
        this.countryOfOrigin="none";
        this.currentOwner=null;
    }

    public artefact(String id, String name, String countryOfOrigin, stakeholder currentOwner){
        this.id=id;
        this.name=name;
        this.countryOfOrigin=countryOfOrigin;
        this.currentOwner=currentOwner;
    }

    public void setId(String id) {this.id=id;}
    public void setName(String name) {this.name=name;}
    public void setCountryOfOrigin(String countryOfOrigin) {this.countryOfOrigin=countryOfOrigin;}
    public void setCurrentOwner(stakeholder currentOwner) {this.currentOwner=currentOwner;}

    public String getId() {return id;}
    public String getName() {return name;}
    public String getCountryOfOrigin() {return countryOfOrigin;}
    public stakeholder getCurrentOwner() {return currentOwner;}

    @Override
    public String toString(){
        String ret = "ID: " + id +"; ";
        ret+= "Name: " + name + "; ";
        ret+= "Country of Origin: " + countryOfOrigin + "; ";
        ret+= "Current Owner: " + currentOwner.getName() + ".";
        return ret;
    }
}

class transaction {
    private artefact item;
    private LocalDateTime timeStamp;
    private stakeholder seller;
    private stakeholder buyer;
    private stakeholder auctionHouse;
    private double price;

    public transaction(){
        this.item = null;
        this.timeStamp = null;
        this.seller = null;
        this.buyer = null;
        this.auctionHouse = null;
        this.price = 0.0;
    }

    public transaction(artefact item, LocalDateTime timeStamp, stakeholder seller, stakeholder buyer, stakeholder auctionHouse, double price){
        this.item = item;
        this.timeStamp = timeStamp;
        this.seller = seller;
        this.buyer = buyer;
        this.auctionHouse = auctionHouse;
        this.price = price;
    }

    public void setItem(artefact item) {this.item=item;}
    public void setTimeStamp(LocalDateTime timeStamp) {this.timeStamp=timeStamp;}
    public void setSeller(stakeholder seller) {this.seller=seller;}
    public void setBuyer(stakeholder buyer) {this.buyer=buyer;}
    public void setAuctionHouse(stakeholder auctionHouse) {this.auctionHouse=auctionHouse;}
    public void setPrice(double price) {this.price=price;}

    public artefact getItem() {return item;}
    public LocalDateTime getTimeStamp() {return timeStamp;}
    public stakeholder getSeller() {return seller;}
    public stakeholder getBuyer() {return buyer;}
    public stakeholder getAuctionHouse() {return auctionHouse;}
    public double getPrice() {return price;}

    @Override
    public String toString(){
        String ret = "Item: " + item.getName() +"; ";
        ret+= "Time Stamp: " + timeStamp +"; ";
        ret+= "Seller: " + seller.getName() + "; ";
        ret+= "Buyer: " + buyer.getName() + "; ";
        ret+= "Auction House: " + auctionHouse.getName() + "; ";
        ret+= "Price: " + price + ".";
        return ret;
    }
}

class block {
    private String prevHash;
    private transaction data;
    private LocalDateTime timeStamp;
    private int nonce;
    private String hash;

    public block(){
        this.prevHash=null;
        this.data=null;
        this.timeStamp=null;
        this.nonce=0;
        this.hash=null;
    }

    public block(String prevHash, transaction data, LocalDateTime timeStamp, int nonce){
        this.prevHash=prevHash;
        this.data=data;
        this.timeStamp=timeStamp;
        this.nonce=nonce;
    }

    public block(String prevHash, transaction data, LocalDateTime timeStamp){
        this.prevHash=prevHash;
        this.data=data;
        this.timeStamp=timeStamp;
    }

    public block(String prevHash, transaction data, LocalDateTime timeStamp, int nonce, String hash){
        this.prevHash=prevHash;
        this.data=data;
        this.timeStamp=timeStamp;
        this.nonce=nonce;
        this.hash=hash;
    }

    public void setPrevHash(String prevHash) {this.prevHash=prevHash;}
    public void setData(transaction data) {this.data=data;}
    public void setTimeStamp(LocalDateTime timeStamp) {this.timeStamp=timeStamp;}
    public void setNonce(int nonce) {this.nonce=nonce;}
    public void setHash(String hash) {this.hash=hash;}

    public String getPrevHash() {return prevHash;}
    public transaction getData() {return data;}
    public LocalDateTime getTimeStamp() {return timeStamp;}
    public int getNonce() {return nonce;}
    public String getHash() {return hash;}

   public String calculateBlockHash(){
        String dataToHash = prevHash
                +timeStamp.toString()
                +String.valueOf(nonce)
                +data.toString();
       MessageDigest digest = null;
       byte[] bytes = null;
       try{
           digest = MessageDigest.getInstance("SHA-256");
           bytes = digest.digest(dataToHash.getBytes("utf-8"));
       } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
           System.out.println("The Encoding is not supported");
       }
       StringBuffer buffer = new StringBuffer();
       for (byte b : bytes){
           buffer.append(String.format("%02x", b));
       }
       return buffer.toString();
   }
}

public class Main {


    // make the hash start with 4 zeroes
    public static boolean mineBlock(int prefix, transaction data, ArrayList<block> BC){
        Random rand = new Random();
        ArrayList<transaction> rpal = retrieveProvenance(data.getItem().getId(), LocalDateTime.of(2001, Month.DECEMBER, 31, 23,59), BC);
        int rpalSize = rpal.size();
        boolean tscMet = treatySC(data, rpalSize, BC);
        if(tscMet){
        // call calculate block hash here
            boolean correctPrefix=false;
            while(!correctPrefix) {
                BC.get(BC.size() - 1).setNonce(rand.nextInt());
                //BC.get(BC.size() - 1).setHash(BC.get(BC.size() - 1).calculateBlockHash());
                String testHash = BC.get(BC.size() - 1).calculateBlockHash();
                if(testHash.substring(0, prefix).equals("0000")){
                    BC.get(BC.size() - 1).setHash(testHash);
                    correctPrefix=true;
                }
            }
           return true;
        } else {
            return false;
        }
    }

    public static boolean treatySC(transaction t, int numTransactionsSinceDate, ArrayList<block> BC){
        if(numTransactionsSinceDate<2 && BC.size()>1){
            System.out.println("This item does not have enough logged transactions since 2001.");
            return false;
        } else {
            if(t.getBuyer().getBalance()<t.getPrice()){
                System.out.println("The buyer does not have enough money to purchase this item.");
                System.out.println("Program closing.");
                System.exit(0);
                return false;
            } else {
                t.getAuctionHouse().setBalance(t.getAuctionHouse().getBalance()+(t.getPrice()/10));
                t.getSeller().setBalance(t.getSeller().getBalance()+((t.getPrice()/10)*7));
                t.getBuyer().setBalance(t.getBuyer().getBalance()-t.getPrice());
                return true;
            }
        }

    }

    public ArrayList<transaction> retrieveProvenance(String id, ArrayList<block> BC){
        ArrayList<transaction> RPT = new ArrayList<transaction>();
        for(int i=0; i<BC.size(); i++){
            if(BC.get(i).getData().getItem().getId()==id){
                RPT.add(BC.get(i).getData());
            }
        }
        return RPT;
    }

    public static ArrayList<transaction> retrieveProvenance(String id, LocalDateTime timeStamp, ArrayList<block> BC){
        ArrayList<transaction> RPT = new ArrayList<transaction>();
        for(int i=0; i<BC.size(); i++){
            if(BC.get(i).getData().getItem().getId().equals(id) && BC.get(i).getData().getTimeStamp().isAfter(timeStamp)){
                RPT.add(BC.get(i).getData());
            }
        }
        return RPT;
    }

    public static boolean verify_Blockchain(ArrayList<block> BC, boolean mined){
        if(!mined){
            System.out.println("Block is not mined.");
            return false;
        } else if(!BC.get(BC.size()-1).getHash().equals(BC.get(BC.size()-1).calculateBlockHash())){
            System.out.println("Wrong hash.");
            return false;
        } else if(BC.size()==1 && BC.get(0).getPrevHash().equals("0")){
            return true;
        } else if(BC.size()==1 && !BC.get(0).getPrevHash().equals("0")){
            System.out.println("Wrong previous hash. 1");
            return false;
        } else if(!BC.get(BC.size()-1).getPrevHash().equals(BC.get(BC.size()-2).getHash())){
            System.out.println("Wrong previous hash. 2");
        } else {
            return true;
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner keyboard = new Scanner(System.in);
        FileInputStream stakeholders = new FileInputStream("C:\\Users\\Camer\\IdeaProjects\\FinalProjectSmithv2\\stakeholders.txt");
        Scanner fileReaderSH = new Scanner(stakeholders);
        String purge;

        Random rand = new Random();
        ArrayList<block> blockchain = new ArrayList<block>();
        ArrayList<block> BC = new ArrayList<block>();
        int prefix = 4;
        String prefixString = new String(new char[prefix]).replace('\0','0');
        ArrayList<stakeholder> shal = new ArrayList<stakeholder>();

        if(!fileReaderSH.hasNextLine()) {
            stakeholder jamesCook = new stakeholder("sh0", "James Cook", "HMS Endeavour", 1000000);
            shal.add(jamesCook);
        }

//        stakeholder sh1 = new stakeholder("sh01", "Cameron", "Wake", 1000);
//        stakeholder sh2 = new stakeholder("sh02", "Connor", "Suffield", 2000);
//        stakeholder sh3 = new stakeholder("ah01", "Sotheby's", "NY, NY", 1000000);
//        stakeholder sh4 = new stakeholder("sh03", "Michele", "Winthrop", 1500);
//        stakeholder sh5 = new stakeholder("sh04", "Scott", "Intervale", 1200);
//        artefact a1 = new artefact("ar01", "Jeep", "USA", sh1);
//        transaction data1 = new transaction(a1, LocalDateTime.now(), sh1, sh2, sh3, 400);
//        transaction data2 = new transaction(a1, LocalDateTime.now(), sh2, sh4, sh3, 3000);
//        transaction data3 = new transaction(a1, LocalDateTime.now(), sh4, sh5, sh3, 500);

        while(fileReaderSH.hasNextLine()){
            stakeholder shTEMP = new stakeholder();
            String data=fileReaderSH.nextLine();
            int a = data.indexOf(":");
            int b = data.indexOf(":", a + 1);
            int c = data.indexOf(":", b + 1);
            int d = data.length();

            shTEMP.setId(data.substring(0,a));
            shTEMP.setName(data.substring(a+1,b));
            shTEMP.setAddress(data.substring(b+1, c));
            shTEMP.setBalance(Double.parseDouble(data.substring(c+1, d)));

            shal.add(shTEMP);

        }

        boolean menu=true;
        while(menu) {
            System.out.println();
            System.out.println("***************************************************************");
            System.out.println("Welcome to the artifact tracker!");
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println();
            System.out.println("Add a stakeholder: 1");
            System.out.println("Attempt a transaction: 2");
            System.out.println("Exit: 3");
            System.out.println();
            int menuChoice=4;
            try {
                menuChoice = keyboard.nextInt();
            } catch (InputMismatchException ime1){
            }
            purge = keyboard.nextLine();
            if(menuChoice==1) {

                String shID = "sh";
                int idNumber = shal.size();
                String idNS = String.valueOf(idNumber);
                shID = shID.concat(idNS);
                String shName = "empty";
                String shAddress = "empty";
                double shBalance = 0;
                    System.out.println("*********************************************************");
                    System.out.println("Add a stakeholder: ");
                    System.out.println("input stakeholder name: ");
                    shName = keyboard.nextLine();
                    System.out.println("input stakeholder address: ");
                    shAddress = keyboard.nextLine();
                    shBalance=-1;
                    boolean validBalance=false;
                    while(!validBalance) {
                        System.out.println("input stakeholder balance: ");
                        try {
                            shBalance = keyboard.nextDouble();
                        } catch (InputMismatchException ime2) {
                            System.out.println("Invalid Balance");
                            shBalance=-1;
                            purge=keyboard.nextLine();
                        }
                        if(shBalance>0){
                            validBalance=true;
                        }
                    }

                stakeholder shTEMP = new stakeholder(shID, shName, shAddress, shBalance);
                shal.add(shTEMP);

                FileOutputStream stakeHolders = new FileOutputStream("stakeholders.txt");
                PrintWriter fileWriter = new PrintWriter(stakeHolders);
                for (int i = 0; i < shal.size(); i++) {
                    fileWriter.println(shal.get(i).getId() + ":" + shal.get(i).getName() + ":" + shal.get(i).getAddress() + ":" + shal.get(i).getBalance());
                }
                fileWriter.close();
                System.out.println("StakeHolder added.");

            } else if (menuChoice==2){
                System.out.println("Attempt transactions");
                menu=false;
            } else if(menuChoice==3){
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid menu option.");
            }
        }

        artefact oilLamp = new artefact("af01", "Oil Lamp", "Fiji", shal.get(0));
        System.out.println();
        System.out.println("****************************************************************************");
        stakeholder i1=null;
        boolean buyerFound=false;
        while(!buyerFound) {
            System.out.println("James Cook currently owns the Oil Lamp.");
            System.out.println("input the ID of the stakeholder who intends to purchase the artifact.");
        String buyerID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(buyerID)) {
                    i1 = shal.get(i);
                    buyerFound = true;
                }
            }
            if (!buyerFound) {
                System.out.println("invalid buyer ID.");
            }
        }

        stakeholder ah=null;
        boolean ahFound=false;
        while(!ahFound) {
            System.out.println("input the ID of the auction house.");
            String ahID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(ahID)) {
                    ah = shal.get(i);
                    ahFound = true;
                }
            }
            if (!ahFound) {
                System.out.println("invalid auction house.");
            }
        }

        double price=-1;
        boolean validPrice=false;
        while(!validPrice) {
            System.out.println("Input the transaction price.");
            try {
                price = keyboard.nextDouble();
            } catch (InputMismatchException ime3) {
                System.out.println("Invalid transaction price.");
                price=-1;
                purge = keyboard.nextLine();
            }
            if(price>0){
                validPrice=true;
            }
        }

        System.out.println("Processing...");
        transaction data1 = new transaction(oilLamp, LocalDateTime.now(), shal.get(0), i1, ah, price);

        block genesisBlock = new block("0", data1, LocalDateTime.now());
        BC.add(genesisBlock);

        //genesisBlock.setHash(genesisBlock.calculateBlockHash());
        boolean genesisMined = mineBlock(prefix, data1, BC);

        if(genesisBlock.getHash().substring(0,prefix).equals(prefixString) && verify_Blockchain(BC, genesisMined)){
            blockchain.add(genesisBlock);
            oilLamp.setCurrentOwner(i1);
            System.out.println("Transaction Successful!");
        } else {
            System.out.println("Malicious block, not added to the chain.");
            BC.remove(BC.get(BC.size()-1));
        }

        System.out.println();
        System.out.println("****************************************************************************");
        stakeholder i2=null;
        buyerFound=false;
        while(!buyerFound) {
            System.out.println("The owner of the Oil Lamp is: " + oilLamp.getCurrentOwner().getId());
            System.out.println("input the ID of the stakeholder who intends to purchase the artifact.");
            String buyerID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(buyerID)) {
                    i2 = shal.get(i);
                    buyerFound = true;
                }
            }
            if (!buyerFound) {
                System.out.println("invalid buyer ID.");
            }
        }

        ah=null;
        ahFound=false;
        while(!ahFound) {
            System.out.println("input the ID of the auction house.");
            String ahID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(ahID)) {
                    ah = shal.get(i);
                    ahFound = true;
                }
            }
            if (!ahFound) {
                System.out.println("invalid auction house.");
            }
        }

        price=-1;
        validPrice=false;
        while(!validPrice) {
            System.out.println("Input the transaction price.");
            try {
                price = keyboard.nextDouble();
            } catch (InputMismatchException ime3) {
                System.out.println("Invalid transaction price.");
                price=-1;
               purge = keyboard.nextLine();
            }
            if(price>0){
                validPrice=true;
            }
        }

        System.out.println("Processing...");
        transaction data2 = new transaction(oilLamp, LocalDateTime.now(), i1, i2, ah, price);

        block secondBlock = new block(blockchain.get(blockchain.size()-1).getHash(), data2, LocalDateTime.now(), rand.nextInt());
        BC.add(secondBlock);
        //secondBlock.setHash(secondBlock.calculateBlockHash());
        boolean secondMined;
        secondMined = mineBlock(prefix, data2, BC);
        if(secondBlock.getHash().substring(0,prefix).equals(prefixString) && verify_Blockchain(BC, secondMined)){
            blockchain.add(secondBlock);
            oilLamp.setCurrentOwner(i2);
            System.out.println("Transaction Successful!");
        } else {
            System.out.println("Malicious block, not added to the chain.");
            BC.remove(BC.get(BC.size()-1));
        }

        System.out.println();
        System.out.println("****************************************************************************");
        stakeholder i3=null;
        buyerFound=false;
        while(!buyerFound) {
            System.out.println("The owner of the Oil Lamp is: " + oilLamp.getCurrentOwner().getId());
            System.out.println("input the ID of the stakeholder who intends to purchase the artifact.");
            String buyerID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(buyerID)) {
                    i3 = shal.get(i);
                    buyerFound = true;
                }
            }
            if (!buyerFound) {
                System.out.println("invalid buyer ID.");
            }
        }

        ah=null;
        ahFound=false;
        while(!ahFound) {
            System.out.println("input the ID of the auction house.");
            String ahID = keyboard.next();
            for (int i = 0; i < shal.size(); i++) {
                if (shal.get(i).getId().equals(ahID)) {
                    ah = shal.get(i);
                    ahFound = true;
                }
            }
            if (!ahFound) {
                System.out.println("invalid auction house.");
            }
        }

        price=-1;
        validPrice=false;
        while(!validPrice) {
            System.out.println("Input the transaction price.");
            try {
                price = keyboard.nextDouble();
            } catch (InputMismatchException ime3) {
                System.out.println("Invalid transaction price.");
                price=-1;
                purge = keyboard.nextLine();
            }
            if(price>0){
                validPrice=true;
            }
        }

        System.out.println("Processing...");
        transaction data3 = new transaction(oilLamp, LocalDateTime.now(), i2, i3, ah, price);

        block newBlock = new block(blockchain.get(blockchain.size()-1).getHash(), data3, LocalDateTime.now(), rand.nextInt());
        BC.add(newBlock);
        //newBlock.setHash(newBlock.calculateBlockHash());
        boolean newMined;
        newMined = mineBlock(prefix, data3, BC);
        if(newBlock.getHash().substring(0,prefix).equals(prefixString) && verify_Blockchain(BC, newMined)){
            blockchain.add(newBlock);
            oilLamp.setCurrentOwner(i3);
            System.out.println("Transaction Successful!");
        } else {
            System.out.println("Malicious block, not added to the chain.");
            BC.remove(BC.get(BC.size()-1));
        }

        FileOutputStream stakeHolders = new FileOutputStream("stakeholders.txt");
        PrintWriter fileWriter = new PrintWriter(stakeHolders);
        for (int i = 0; i < shal.size(); i++) {
            fileWriter.println(shal.get(i).getId() + ":" + shal.get(i).getName() + ":" + shal.get(i).getAddress() + ":" + shal.get(i).getBalance());
        }
        fileWriter.close();
    }
}

// C:\Users\Camer\IdeaProjects\FinalProjectSmithv2\stakeholders.txt