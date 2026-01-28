function showTip() {
    document.getElementById("tip").innerHTML =
        "Always plan your travel budget and book tickets early!";
}

function validateForm() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let destination = document.getElementById("destination").value;

    if (name === "" || email === "" || destination === "") {
        alert("Please fill all the details");
        return false;
    }

    alert("Your travel booking request has been submitted!");
    return true;
}
