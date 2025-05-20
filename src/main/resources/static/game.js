function setupBoard() {
    let board = document.getElementById("board");
    let columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
    for (let row = 8; row > 0; row--) {
        for (let column = 0; column < 8; column++) {
            const square = document.createElement("section");

            // ID como "A1", "B3", etc.
            square.id = `${columns[column]}${row}`;

            // Alternar color blanco/negro
            if ((column + row) % 2 === 0) {
                square.className = "square light";
            } else {
                square.className = "square dark";
            }

            board.appendChild(square);
        }
    }
}

function renderBoard(board){
    let columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

    for (let row = 0; row < 8; row++) {
        for (let column = 0; column < 8; column++) {
            square = document.getElementById(columns[column]+(row+1));

            piece = document.createElement("p")
            piece.innerHTML = board[row][column];
            square.appendChild(piece);
        }
    }
}

const urlParams = new URLSearchParams(window.location.search);
const gameId = urlParams.get("id");

async function loadGame(id) {
    const response = await fetch(`/api/game/${id}/board`);
    const board = await response.json();
    renderBoard(board);
    console.log(board);
}

setupBoard();
loadGame(gameId);