package Drikkehorn.Services;

import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.AdministrativeUnit;
import com.microsoft.graph.requests.GraphServiceClient;

import okhttp3.Request;

public class AdministrativeUnitService {
    private final GraphServiceClient<Request> _graphClient;

    public AdministrativeUnitService(final GraphServiceClient<Request> graphClient) {
        _graphClient = graphClient;
    }

    public AdministrativeUnit AddAdministrativeUnit(final AdministrativeUnit administrativeUnit) {
        return _graphClient
            .directory()
            .administrativeUnits()
            .buildRequest()
            .post(administrativeUnit);
    }

    public DirectoryObject AddAdministrativeUnitMember(
        final AdministrativeUnit administrativeUnit, 
        final DirectoryObject directoryObject
    ) {
        return _graphClient
            .directory()
            .administrativeUnits()
            .byId(administrativeUnit.id)
            .members()
            .references()
            .buildRequest()
            .post(directoryObject);
    }
}
