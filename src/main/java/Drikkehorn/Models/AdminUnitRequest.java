package Drikkehorn.Models;

public class AdminUnitRequest {
    private String userPrincipalNameFilter;
    private String groupDisplayName;
    private String administrativeUnitDisplayName;
    
    public String getUserPrincipalNameFilter() {
        return this.userPrincipalNameFilter;
    }

    public String getGroupDisplayName() {
        return this.groupDisplayName;
    }

    public String getAdministrativeUnitDisplayName() {
        return this.administrativeUnitDisplayName;
    }
}
