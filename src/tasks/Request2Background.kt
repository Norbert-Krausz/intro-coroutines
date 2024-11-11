package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User
import kotlin.concurrent.thread

fun loadContributorsBackground(service: GitHubService, req: RequestData, updateResults: (List<User>) -> Unit) {
    thread {

        //val result = loadContributorsBlocking(service, req)
        //updateResults(result)

        updateResults(loadContributorsBlocking(service, req))
    }
}