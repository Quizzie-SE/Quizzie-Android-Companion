package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.util.BACKEND_AUTH_TOKEN_TYPE
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
//        val authToken: AuthToken = sessionManager.getToken()
//        synchronized(this) {
//            if (responseCount(response) >= 2) {
//                // If both the original call and the call with refreshed token failed,
//                // it will probably keep failing, so don't try again and logout
//                sessionManager.truncateSession()
//                Timber.d("authenticate: ")
//                return null
//            }
//            val authToken1: AuthToken = sessionManager.getToken()
//            return if (!authToken.equals(authToken1)) {
//                //Token Already refreshed
//                createNewRequestWithHeader(
//                    response,
//                    authToken1.getAccessToken()
//                )
//            } else try {
//                val tokenResponse: retrofit2.Response<BackendResponse<UserData>> =
//                    authRepository.refreshAccessToken(
//                        authToken.getAccessToken(),
//                        authToken.getRefreshToken()
//                    )
//                if (tokenResponse.code() == 200) {
//                    val newToken: AuthToken = tokenResponse
//                        .body()
//                        .getData()
//                        .getToken()
//                    sessionManager.addToken(newToken)
//                    createNewRequestWithHeader(
//                        response,
//                        newToken.getAccessToken()
//                    )
//                } else if (tokenResponse.code() == 401) {
//                    sessionManager.truncateSession()
//                    null
//                } else {
//                    null
//                }
//            } catch (e: IOException) {
//                null
//            }
//
//            // We need a new client, since we don't want to make another call using our client with access token
//        }

        return null
    }

    companion object {
        private const val TAG = "TokenAuthenticator"
        private fun createNewRequestWithHeader(
            response: Response,
            accessToken: String
        ): Request {
            return response
                .request
                .newBuilder()
                .header("Authorization", "$BACKEND_AUTH_TOKEN_TYPE $accessToken")
                .build()
        }

        private fun responseCount(response: Response): Int {
            var response = response
            var result = 1
            while (response.priorResponse.also { response = it!! } != null) {
                result++
            }
            return result
        }
    }


}