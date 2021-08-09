package Drikkehorn.Services;

import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.Group;
import com.microsoft.graph.requests.GraphServiceClient;

import okhttp3.Request;

public class GroupService {
    private final GraphServiceClient<Request> _graphClient;

    public GroupService(final GraphServiceClient<Request> graphClient) {
        _graphClient = graphClient;
    }

    public Group AddGroup(final Group group) {
        return _graphClient
            .groups()
            .buildRequest()
            .post(group);
    }

    public DirectoryObject AddGroupMember(
        final Group group, 
        final DirectoryObject directoryObject
    ) {
        return _graphClient
            .groups()
            .byId(group.id)
            .members()
            .references()
            .buildRequest()
            .post(directoryObject);
    }
}
