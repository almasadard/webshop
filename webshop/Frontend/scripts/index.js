// Kaffee Counter
const counters = document.querySelectorAll("#kaffee .counter, #tee .counter, #spices .counter");

counters.forEach((counter) => {
    const plusBtn = counter.querySelector(".plus");
    const minusBtn = counter.querySelector(".minus");
    const countEl = counter.querySelector(".count");

    let count = 0;

    plusBtn.addEventListener("click", () => {
        count++;
        countEl.textContent = count;
    });

    minusBtn.addEventListener("click", () => {
        if (count > 0) {
            count--;
            countEl.textContent = count;
        }
    });
});

//Zum Warenkorb hinzufügen

const addToCartButtons = document.querySelectorAll(".btn-block");


addToCartButtons.forEach((button) => {
    button.addEventListener("click", () => {
        // hier kommt der POST-Request
        // z.B. mit fetch()

        // Alert ausgeben
        alert("Zum Warenkorb hinzugefügt");
    });
});


/*addToCartButtons.forEach((button) => {
    button.addEventListener("click", () => {
        // hier kommt der POST-Request
        // z.B. mit fetch()

        // Weiterleitung zur Warenkorbseite
        window.location.href = "Warenkorb.html";
    });
});*/