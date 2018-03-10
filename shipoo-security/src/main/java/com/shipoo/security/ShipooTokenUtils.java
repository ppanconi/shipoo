package com.shipoo.security;

abstract class ShipooTokenUtils  {

    public interface TokenManager {

        String generateTokenForUser(ShipooTokenUser user);

        ShipooTokenUser buildUserFromToken(String token);
    }

    public static TokenManager getManager() {
        return new JWTTockenManager();
    }

    /*
       Private implemetations
     */

    private static class JWTTockenManager implements TokenManager {

        @Override
        public String generateTokenForUser(ShipooTokenUser user) {
            return null;
        }

        @Override
        public ShipooTokenUser buildUserFromToken(String token) {
            return null;
        }
    }

}
