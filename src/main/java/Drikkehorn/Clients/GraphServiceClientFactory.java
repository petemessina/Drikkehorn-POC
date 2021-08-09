package Drikkehorn.Clients;

import java.util.List;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;

import okhttp3.Request;

/*
 * Factory class for creating a graph service client.
 */
public class GraphServiceClientFactory {

    /*
     * Creates a client secret based graph service client
     */
    public GraphServiceClient<Request> createGraphServiceClient(
        final String clientId,
        final String clientSecret,
        final String tenantId,
        final List<String> scopes
    ) {
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .tenantId(tenantId)
                    .build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
        final GraphServiceClient<Request> graphClient = GraphServiceClient
            .builder()
            .authenticationProvider(tokenCredentialAuthProvider)
            .buildClient();

        return graphClient;
    }
}
