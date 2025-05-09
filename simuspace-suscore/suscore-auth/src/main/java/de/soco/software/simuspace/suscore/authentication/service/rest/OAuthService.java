package de.soco.software.simuspace.suscore.authentication.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface OAuthService {

    @GET
    @Path( "/oauth_directories" )
    @Produces( MediaType.APPLICATION_JSON )
    Response oauthUserDirectoriesList();

    @GET
    @Path( "/login" )
    @Produces( MediaType.APPLICATION_JSON )
    Response oauthAuthorize( @QueryParam( "directory_id" ) UUID directoryId );

    @GET
    @Path( "/access_token" )
    @Produces( MediaType.APPLICATION_JSON )
    Response oauthAccessToken( @QueryParam( "state" ) String state,
            @QueryParam( "code" ) String code,
            @QueryParam( "scope" ) String scope,
            @QueryParam( "authuser" ) String authuser,
            @QueryParam( "prompt" ) String prompt );

    // {http://localhost:8181/api/oauth117782947372408146986/token/ya29.a0AXeO80T1EWkhme2WtmkI4U2O6qhvX4MbpfO6aJjlyRLpy2JTiS5p7hONZ9f6i-BE7PkeqEQuoBbcFpUNunuTQMKprp2jZDMp866F0UXu8RcR2XRD09CARu1DjM7cKKVObwvdjEVpOt_Q-1RaajHcM1BZa-Hn-G3Cp0akN6LRaCgYKAU8SARESFQHGX2MiGn2-mHsaywlb1lSDwdXSeQ0175/refreshToken/null/token_id/eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc2M2Y3YzRjZDI2YTFlYjJiMWIzOWE4OGY0NDM0ZDFmNGQ5YTM2OGIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMDYzNjEzMjkzNTY4LTBhbDBwamd2Z3IzMGNycHA2OGk3MDdsdWJvNmowNHF1LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA2MzYxMzI5MzU2OC0wYWwwcGpndmdyMzBjcnBwNjhpNzA3bHVibzZqMDRxdS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExNzc4Mjk0NzM3MjQwODE0Njk4NiIsImVtYWlsIjoibXVzdGFmYW5vb3I3MTVAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiIyQ3E3T09pZEVleEhLLVk2UEtSaXp3Iiwibm9uY2UiOiJub25jZV9PSURDIiwibmFtZSI6Ik5vb3IgTXVzdGFmYSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NMaEJzWkNuRVZpRmIzaUdJZGdlTDZCaFpjUWM0czZBZ3lEVWJwdGVYZ2hFNEFVTUJKVj1zOTYtYyIsImdpdmVuX25hbWUiOiJOb29yIiwiZmFtaWx5X25hbWUiOiJNdXN0YWZhIiwiaWF0IjoxNzQwNTYyNDY0LCJleHAiOjE3NDA1NjYwNjR9.t3TFp37J4QK124cV3_rTPHzCOFqtc5Pm9Y2L0trZa4yEpWYToe75aiWy_pjasvakvKTEna7DNufw9ukTpQBXaUNPEtSbFfO5gYzlZa-vswriLeGtsLuImznhFR5KZbVfrDtBZdJMBkp1ra4My8FB8Z8TZOhJqTcvaGnwYZXI5gDHfaBnXEIklWl1r4vEB8QlFu0sweSOoVUxyQLk8ktTW-dAg1DrUtKoHtPqOHCpqKZphlpuhnk71FNHTNuIZMoJHT4P-Grz4PVjqpGeVxSgI7lIdHvbnfhDBQU02MGgcfB6EP4z14KxHcF1S-ajgGahqtTcBXRCjNCHQcizAsdldA/internal-state/state_OIDC }
    @GET
    @Path( "/{qNumber}/token/{x_oauth_token}/refreshToken/{oauth_refresh_token}/token_id/{oauth_token_id}/state/{state}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkUserAndRegister( @PathParam( "qNumber" ) String qNumber, @PathParam( "x_oauth_token" ) String x_oauth_token,
            @PathParam( "oauth_refresh_token" ) String oauth_refresh_token, @PathParam( "oauth_token_id" ) String oauth_token_id,
            @PathParam( "state" ) String state );

}
