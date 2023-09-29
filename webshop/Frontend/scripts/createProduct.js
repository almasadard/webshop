document.addEventListener('DOMContentLoaded', function () {
    const formCreateProduct = document.getElementById('formCreateProduct');
    const descriptionInput = document.getElementById('description');

    formCreateProduct.addEventListener('submit', function (event) {
        event.preventDefault();

        //Ceckboxen überprüfen

        const productName = document.getElementById('inputProductName').value;
        const productPrice = document.getElementById('inputProductPrice').value;
        const productQuantity = document.getElementById('inputProductQuantity').value;
        const productDescription = descriptionInput.value;

        const fileInput = document.getElementById("inputProductImage");
        const file = fileInput.files[0];

        console.log(file);

        const formData = new FormData();
        formData.append("file", file);

        console.log(formData);

        $.ajax({
            url: 'http://localhost:8080/files',
            type: 'POST',
            contentType: false, // Setze contentType auf false, damit FormData den richtigen Header setzt
            processData: false, // Setze processData auf false, damit FormData nicht den Inhalt formatiert
            headers: { "Authorization": sessionStorage.getItem("token") },
            data: formData,
            success: function (response) {
                console.log('File erfolgreich gesendet:', response);
                alert('File erfolgreich gesendet!');

                // Erhalte den Namen des hochgeladenen Bildes aus der Antwort und erstelle die imageUrl
                const imageName = response;
                const imageUrl = imageName;

                // Hier senden wir die restlichen Produktinformationen an den Server
                const product = {
                    name: productName,
                    description: productDescription,
                    price: productPrice,
                    quantity: productQuantity,
                    imageUrl: imageUrl,
                    active: false,
                };

                $.ajax({
                    url: 'http://localhost:8080/products',
                    type: 'POST',
                    contentType: 'application/json',
                    headers: { "Authorization": sessionStorage.getItem("token") },
                    data: JSON.stringify(product),
                    success: function (response) {
                        console.log('Daten erfolgreich gesendet:', response);
                        alert('Daten erfolgreich gesendet!');
                        loadProducts();
                    },
                    error: function (xhr, status, error) {
                        console.error('Fehler beim Senden der Daten:', error);
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error('Fehler beim Senden des Bildes:', error);
            }
        });
    });
});