const urlParams = new URLSearchParams(window.location.search);
const gameId = urlParams.get("id");

function setupBoard() {
    let board = document.getElementById("board");
    let columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
    for (let row = 8; row > 0; row--) {
        for (let column = 0; column < 8; column++) {
            let square = document.createElement("section");

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
        square.addEventListener("drop", async evento => {
            const id = evento.dataTransfer.getData("id");
            const startPos = evento.dataTransfer.getData("startPos");

            if(await movePiece(startPos, square.id)){
                if (id) { 
                    const piece = document.getElementById(id);
                    square.replaceChildren(piece);
                }
            }
        })
        square.addEventListener("contextmenu", event =>{
            event.preventDefault();
        })
    })
}

function renderBoard(board) {
    let columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

    for (let row = 0; row < 8; row++) {
        for (let column = 0; column < 8; column++) {
            let pieceCode = board[row][column];
            if (pieceCode != null) {
                let square = document.getElementById(columns[column] + (row + 1));
                square.replaceChildren(createPiece(pieceCode));
            }
        }
    }

    let pieces = document.querySelectorAll(".piece");
    pieces.forEach(piece => {
        piece.addEventListener("dragstart", event => {
            piece.classList.add("dragged");
            
            event.dataTransfer.setData("id", piece.id);
            event.dataTransfer.setData("startPos", piece.parentElement.id);
        })
        piece.addEventListener("dragend", event => {
            setTimeout(() => {
                piece.classList.remove("dragged");
            }, 100);
        })
        piece.addEventListener("contextmenu", event =>event.preventDefault())
    })
}

async function loadGame(id) {
    const response = await fetch(`/api/game/${id}/board`);
    const game = await response.json();
    renderBoard(game.board);
}

function createPiece(pieceCode) {
    const piece = document.createElement("img");
    piece.draggable = true;
    piece.classList.add("piece");

    const color = pieceCode[0] === 'W' ? "white" : "black";
    const type = {
        P: "pawn",
        N: "knight",
        B: "bishop",
        R: "rook",
        Q: "queen",
        K: "king"
    }[pieceCode[1]];

    piece.src = `assets/${color}_${type}.png`;
    piece.classList.add(color, type);
    piece.id = pieceCode;

    return piece;
}

async function movePiece(startPosArg, endPosArg){
    const  data = {
        startPos: startPosArg,
        endPos: endPosArg
    }
    console.log("Enviando:", JSON.stringify(data));

    try{
        const response = await fetch(`/api/game/${gameId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        if(!response.ok){
            console.log(response);
            return false;
        }
    }
    catch (error){
        console.error("Error: ", error);
        return false;
    }
    return true;
}

const titleElement = document.getElementById("game-title");
titleElement.textContent = `Game #${gameId}`;
setupBoard();
loadGame(gameId);