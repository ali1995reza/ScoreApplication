package main

import (
	"fmt"
	score_app "impl-score-app-server/score-app"
	in_memory_repositories "impl-score-app-server/score-app/impl/repositories"
	jwt_auth_service "impl-score-app-server/score-app/impl/services"
	"impl-score-app-server/server"
)

func main() {
	fmt.Print("Server address [[host]:port] : ")
	address := readLine()
	app := score_app.NewScoreApplication(
		jwt_auth_service.NewJwtAuthenticationService("sercret"),
		in_memory_repositories.NewInMemoryUserRepository(),
		in_memory_repositories.NewInMemoryScoreRepository(),
	)
	server.NewScoreApplicationServer(address, app).Run()

}

func readLine() string {
	var str string
	_, err := fmt.Scanln(&str)
	if err != nil {
		panic(err)
	}
	return str
}
