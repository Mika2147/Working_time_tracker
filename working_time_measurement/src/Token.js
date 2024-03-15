import { msalConfig, appRoles, protectedResources } from "./Config";
import { msalInstance } from "./index";

export const Token = {
    getToken: async () => {
        const currentAccount = msalInstance.getActiveAccount();
        const accessTokenRequest = {
          scopes: protectedResources.time.scopes,
          account: currentAccount,
        };
      
        if (currentAccount) {
          if (currentAccount.tenantId == msalConfig.auth.tenantId) {
            const roles = (currentAccount.idTokenClaims).roles;
            if (roles) {
              const intersection = Object.keys(appRoles).filter((role) => roles.includes(role));
              if (intersection.length > 0) {
                const accessTokenResponse = await msalInstance.acquireTokenSilent(accessTokenRequest);
                return `Bearer ${accessTokenResponse.accessToken}`;
              }
            }
          }
          return null;
        }
      }
};
