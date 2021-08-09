package Drikkehorn.Services;

import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;

import okhttp3.Request;

public class UserService {

    private final GraphServiceClient<Request> _graphClient;

    public UserService(GraphServiceClient<Request> graphClient) {
        _graphClient = graphClient;
    }

    public User getUser(final String userPricipalName) throws Exception {
        final String filter = String.format("startswith(userPrincipalName, '%s')", userPricipalName);
        final UserCollectionPage userCollectionPage =  _graphClient
            .users()
            .buildRequest()
            .filter(filter)
            .get();
        
        if(userCollectionPage.getCurrentPage().size() != 1) {
            throw new Exception("User principal name was not found");
        }

        return userCollectionPage.getCurrentPage().get(0);
   } 
}
