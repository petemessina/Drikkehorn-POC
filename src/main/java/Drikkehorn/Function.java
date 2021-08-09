package Drikkehorn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.graph.models.AdministrativeUnit;
import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.Group;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;

import Drikkehorn.Clients.GraphServiceClientFactory;
import Drikkehorn.Models.AdminUnitRequest;
import Drikkehorn.Services.AdministrativeUnitService;
import Drikkehorn.Services.GroupService;
import Drikkehorn.Services.UserService;

import okhttp3.Request;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    private GraphServiceClient<Request> _graphClient;

    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<AdminUnitRequest> request,
            final ExecutionContext context) throws Exception {
        
        createGraphServiceClient();

        final AdminUnitRequest adminUnitRequest = request.getBody();
        final UserService userService = new UserService(_graphClient);
        final User aadUser = userService.getUser(adminUnitRequest.getUserPrincipalNameFilter());
        final Group aadGroup = createGroupWithUser(aadUser, adminUnitRequest.getGroupDisplayName());        
        final AdministrativeUnit aadAdministativeUnit = createAdministrativeUnitWithGroup(aadGroup, adminUnitRequest.getAdministrativeUnitDisplayName());
                
        return request
            .createResponseBuilder(HttpStatus.OK)
            .body(aadAdministativeUnit.id)
            .build();
    }

    /*
     * Used to create a graph service client based on system environment variables. 
     */
    private void createGraphServiceClient() {
        final GraphServiceClientFactory graphServiceClientFactory = new GraphServiceClientFactory();
        
        final String clientId = System.getenv("ClientId");
        final String clientSecret = System.getenv("ClientSecret");
        final String tenantId = System.getenv("TenantId");
        final List<String> scopes = Arrays.asList(new String[]{"https://graph.microsoft.com/.default"});
        
        _graphClient = graphServiceClientFactory
            .createGraphServiceClient(clientId, clientSecret, tenantId, scopes);
    }

    /*
     * Used to create a default group and add a member to it 
     */
    private Group createGroupWithUser(
        final DirectoryObject directoryObject,
        final String displayName
    ) {
        final GroupService groupService = new GroupService(_graphClient);
        Group aadGroup = new Group();

        aadGroup.displayName = displayName;
        aadGroup.mailEnabled = false;
        aadGroup.mailNickname = "Test";
        aadGroup.securityEnabled = true;
        aadGroup = groupService.AddGroup(aadGroup);

        groupService.AddGroupMember(aadGroup, directoryObject);

        return aadGroup;
    }

    /*
     * Used to create a administrative unit and add a group to it 
     */
    private AdministrativeUnit createAdministrativeUnitWithGroup(
        final DirectoryObject directoryObject,
        final String displayName
    ) {
        final AdministrativeUnitService administrativeUnitService = new AdministrativeUnitService(_graphClient);
        AdministrativeUnit aadAdministrativeUnit = new AdministrativeUnit();

        aadAdministrativeUnit.displayName = displayName;
        aadAdministrativeUnit = administrativeUnitService.AddAdministrativeUnit(aadAdministrativeUnit);

        administrativeUnitService.AddAdministrativeUnitMember(aadAdministrativeUnit, directoryObject);

        return aadAdministrativeUnit;
    }
}
