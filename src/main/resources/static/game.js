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

    let squares = document.querySelectorAll(".square");
    squares.forEach(square => {
        square.addEventListener("dragover", event => {
            event.preventDefault();
        })
        square.addEventListener("drop", evento => {
            const id = evento.dataTransfer.getData("id");
            const piece = document.getElementById(id);
            if (square.firstElementChild) {
                square.removeChild(square.firstElementChild);
            }
            square.append(piece);
        })
    })
}

function renderBoard(board) {
    let columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

    for (let row = 0; row < 8; row++) {
        for (let column = 0; column < 8; column++) {
            pieceCode = board[row][column];
            if (pieceCode != null) {
                square = document.getElementById(columns[column] + (row + 1));

                piece = document.createElement("p");
                piece.draggable = true;
                piece.classList.add("piece");

                color = pieceCode[0] === 'W' ? "white" : "black";
                type = {
                    P: "pawn",
                    N: "knight",
                    B: "bishop",
                    R: "rook",
                    Q: "queen",
                    K: "king"
                }[pieceCode[1]];

                piece.classList.add(color)
                piece.classList.add(type)
                
                piece.id = pieceCode;
                square.appendChild(piece);
            }
        }
    }

    let pieces = document.querySelectorAll(".piece");
    pieces.forEach(piece => {
        piece.addEventListener("dragstart", event => {
            piece.classList.add("dragged");
            event.dataTransfer.setData("id", piece.id);
        })
        piece.addEventListener("dragend", event => {
            piece.classList.remove("dragged");
        })
    })
}

const urlParams = new URLSearchParams(window.location.search);
const gameId = urlParams.get("id");

async function loadGame(id) {
    const response = await fetch(`/api/game/${id}/board`);
    const game = await response.json();
    renderBoard(game.board);
}

setupBoard();
loadGame(gameId);