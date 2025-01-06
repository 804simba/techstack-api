package com.techstack.api.commons;

public interface ApiRoute {
    String BASE = "/api/v1";

    interface Accounts {
        String BASE = ApiRoute.BASE + "/accounts";
        String NEW = "/new";
        String ACTIVATE = "/{accountId}/activate";
        String ACCOUNT_INFORMATION = "/{accountId}/account";
        String SEARCH = "/search";
        String GET_ACCOUNTS = "/{userId}/accounts";
        String DELETE_ACCOUNT = "/{accountId}";
    }

    interface AppUser {
        String BASE = ApiRoute.BASE + "/users";
        String NEW = "/new";
        String ALL = "/all";
    }

    interface Products {
        String BASE = ApiRoute.BASE + "/products";
        String ALL = "/all";
    }
}
