let timer;
let timeLeft = 300; // 5 minutes

function startTimer() {
  clearInterval(timer);
  timeLeft = 300;
  timer = setInterval(() => {
    let minutes = Math.floor(timeLeft / 60);
    let seconds = timeLeft % 60;
    document.getElementById("countdown").innerText =
      `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    if (timeLeft <= 0) {
      clearInterval(timer);
      document.getElementById("countdown").innerText = "⏰ Time Up!";
      alert("Contest Time Over!");
    }
    timeLeft--;
  }, 1000);
}

async function submitCode() {
  const code = document.getElementById("codeArea").value;
  if (!code.trim()) {
    alert("Please write some code before submitting!");
    return;
  }

  document.getElementById("output").innerText = "⏳ Submitting code...";

  try {
    const response = await fetch("http://localhost:8080/submit", {
      method: "POST",
      headers: { "Content-Type": "text/plain" },
      body: code
    });

    const result = await response.text();
    document.getElementById("output").innerText = result;
  } catch (error) {
    document.getElementById("output").innerText = "❌ Error connecting to server!";
  }
}
