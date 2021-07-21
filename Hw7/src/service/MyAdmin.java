package service;

import domain.User;
import domain.UserAdmin;
import repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MyAdmin {
    private Scanner scannerForString = new Scanner(System.in);
    private Scanner scannerForInteger = new Scanner(System.in);


    public void loginUserAdminForRegisterUser(User user, App app) throws SQLException {

        System.out.println("enter userName of userAdmin ...");
        String userName = scannerForString.nextLine();
        System.out.println("enter password of userAdmin");
        String password = scannerForString.nextLine();
        for (UserAdmin admin : ApplicationContext.getUserAdminRepository().findAll()) {
            if (admin.getUserName().equals(userName))
                if (admin.getPassWord().equals(password)) {
                    confirmToRegisterUser(user, admin, app);
                    return;
                }

        }

        System.out.println("this admin this this data not exists try again ...");
        loginUserAdminForRegisterUser(user, app);
    }

    public void loginUserAdmin(String userName, String password) throws SQLException {
        for (UserAdmin admin : ApplicationContext.getUserAdminRepository().findAll()) {
            if (admin.getUserName().equals(userName))
                if (admin.getPassWord().equals(password)) {
                    userAdminPanel(admin);
                    return;
                }

        }

        System.out.println("this admin this this data not exists try again ...");
    }

    public void blockAccount(User user) throws SQLException {
        AccountRepository.blockAccount(user);
    }

    public void unBlockAccount(User user, Connection connection) throws SQLException {
        AccountRepository.unBlockAccount(user);
    }

    public void confirmToRegisterUser(User user, UserAdmin admin, App app) throws SQLException {

        System.out.println("user admin with name : " + admin.getName() + " , family : " + admin.getFamily() + " are you want user add  !!!\nif want enter yes else enter no ...");
        String str = scannerForString.nextLine();

        switch (str) {
            case "yes" -> {
                ApplicationContext.getUserRepository().add(user);
                user.setId(ApplicationContext.getUserRepository().size());
                System.out.println("!! registered new user by username : " + user.getUserName() + "\n");
                app.changingPassword(user);
            }
            case "no" -> System.out.println("user admin not allow who register in site  # i'm sorry ...\n");

            default -> {
                System.out.println("your data not valid try again --_--\n");
                confirmToRegisterUser(user, admin, app);
            }

        }


    }

    public void userAdminPanel(UserAdmin admin) throws SQLException {

        System.out.println("welcome userAdmin "+admin.getUserName()+"))\n\n");
        System.out.println("1.show all ordinary user ...");
        System.out.println("2.block user ...");
        System.out.println("3.unBlockUser ...");
        System.out.println("4.exit of user_admin panel ... ");

        switch (scannerForInteger.nextInt()) {

            case 1 -> {
                ApplicationContext.getUserRepository().showAll();
                userAdminPanel(admin);
            }
            case 2 -> {
                System.out.println("enter userName");
                String userName = scannerForString.nextLine();

                User user = ApplicationContext.getUserRepository().checkExistsUser(userName);

                blockAccount(user);
                System.out.println("this account blocked by userAdmin : " + admin.getName() + "   " + admin.getFamily() + "   " + admin.getAge());
                userAdminPanel(admin);
            }
            case 3 -> {
                System.out.println("enter userName");
                String userName = scannerForString.nextLine();

                User user = ApplicationContext.getUserRepository().checkExistsUser(userName);
                unBlockAccount(user, ApplicationContext.getConnection());
                System.out.println("this account  unblocked  by user admin : " + admin.getName() + "   " + admin.getFamily() + "   " + admin.getAge());
                userAdminPanel(admin);
            }
            case 4 -> System.out.println("operation in userPanelAdmin is finished back to home ...\n");

            default -> {
                System.out.println("your data not valid in panel of user_admin panel try again\n");
                userAdminPanel(admin);
            }


        }

    }

    public void confirmToRegisterUserAdmin(UserAdmin oldAmin, UserAdmin newAdmin) throws SQLException {

        System.out.println("user admin with name : " + oldAmin.getName() + " , family : " + oldAmin.getFamily() + " are you want user add  !!!\nif want enter yes else enter no ...");
        String result = scannerForString.nextLine();

        switch (result) {

            case "yes" -> {
                System.out.println("registered new userAdmin " + newAdmin.getUserName() + "  by  admin :" + oldAmin.getName() + "   " + oldAmin.getFamily());
                ApplicationContext.getUserAdminRepository().add(newAdmin);
                userAdminPanel(newAdmin);
            }
            case "no" -> System.out.println("im sorry userAdmin not allow to you add to userAdmin !!!\n");

            default -> {
                System.out.println("your data not valid try again --___--");
                confirmToRegisterUserAdmin(oldAmin, newAdmin);
            }

        }

    }

    public void registerUserAdmin(UserAdmin admin) throws SQLException {

        System.out.println("enter userName of userAdmin for register ...");
        String userAdminName = scannerForString.nextLine();
        System.out.println("enter password of userAdmin for register ...");
        String userAdminPassword = scannerForString.nextLine();

        for (UserAdmin ad : ApplicationContext.getUserAdminRepository().findAll()) {
            if (ad.getUserName().equals(userAdminName))
                if (ad.getPassWord().equals(userAdminPassword)) {
                    confirmToRegisterUserAdmin(ad, admin);
                    return;
                }
        }
        System.out.println("this admin this this data not exists try again because im waiting which admin login...");
        registerUserAdmin(admin);

    }
}



