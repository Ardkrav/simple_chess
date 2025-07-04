async function startNewGame() {
    try {
        const nameInput = document.getElementById("nameInput");
        const passInput = document.getElementById("passInput");
        let gameName = nameInput.value ? nameInput.value : "Simple Chess Game";
        let gamePassword = passInput.value ? passInput.value : null;
        const request = await fetch("/api/games",
            {
                method: "POST",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ name: gameName, password: gamePassword })
            }
        );
        if (!request.ok) {
            let errorText = await request.text();
            throw new Error(`Error: ${request.status} - ${errorText}`)
        }
        const gameId = await request.json();
        console.log(gameId)

        window.location.href = `./game.html?id=${gameId}`;
    } catch (error) {
        console.log(error)
    }
}

async function getGames() {
    try {
        const request = await fetch("/api/games",
            {
                method: "GET",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            }
        )
        if (!request.ok) {
            let errorText = await request.text();
            throw new Error(`Error: ${request.status} - ${errorText}`)
        }
        return request;
    } catch (error) {
        console.log(error)
        return null;
    }
}

async function showGames() {
    response = await getGames();
    games = await response.json();
    console.log(games)
    if(games){
        const tableBody = document.querySelector("#games-table tbody");
        tableBody.innerHTML = "";

        games.forEach(game => {
            const row = document.createElement("tr");

            const idCell = document.createElement("td");
            idCell.textContent = game.id;

            const nameCell = document.createElement("td");
            nameCell.textContent = game.name;

            const passwordCell = document.createElement("td");
            passwordCell.textContent = game.password ? "Sí" : "No";
            if(game.password){
                passwordCell.textContent = "Yes";
                passwordCell.style = "color: #45a049; font-weight: bold;"
            } else {
                passwordCell.textContent = "No";
                passwordCell.style = "color: #d32f2f; font-weight: bold;"
            }

            const actionCell = document.createElement("td");
            const joinButton = document.createElement("button");
            joinButton.textContent = "Join";
            joinButton.className = "join"
            joinButton.onclick = () => {
                window.location.href = `game.html?id=${game.id}`;
            };
            actionCell.appendChild(joinButton);

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.className = "delete"
            deleteButton.addEventListener("click", () => deleteGame(game.id));
            actionCell.appendChild(deleteButton);

            row.appendChild(idCell);
            row.appendChild(nameCell);
            row.appendChild(passwordCell);
            row.appendChild(actionCell);

            tableBody.appendChild(row);
        });
    }
}

async function deleteGame(gameId) {
    try {
        const request = await fetch(`/api/game/${gameId}`,
            {
                method: "DELETE",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            }
        )
        if (!request.ok) {
            let errorText = await request.text();
            throw new Error(`Error: ${request.status} - ${errorText}`)
        }
        showGames();
        return request;
    } catch (error) {
        console.log(error)
        return null;
    }
}

let start = document.getElementById("start");
start.addEventListener("click", event => {
    event.preventDefault();
    startNewGame();
})
showGames();