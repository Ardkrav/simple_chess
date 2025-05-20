async function startNewGame() {
    try {
        const request = await fetch("http://localhost:8080/api/games",
            {
                method: "POST",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
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

let start = document.getElementById("start");
start.addEventListener("click", event => {
    event.preventDefault();
    startNewGame();
})