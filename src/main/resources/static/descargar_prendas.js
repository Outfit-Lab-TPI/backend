import fs from "fs";
import path from "path";
import https from "https";

const urls = [
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/superior/1.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/superior/2.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/superior/3.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/superior/4.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/inferior/1.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/inferior/2.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/inferior/3.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/puma/inferior/4.png",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/superior/1.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/superior/2.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/superior/3.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/superior/4.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/inferior/1.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/inferior/2.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/inferior/3.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/vulk/inferior/4.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/superior/1.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/superior/2.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/superior/3.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/superior/4.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/inferior/1.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/inferior/2.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/inferior/3.jpg",
    "https://outfitlab-bucket.s3.sa-east-1.amazonaws.com/expo/marcas/volcom/inferior/4.jpg",
];

// Carpeta base local
const baseDir = "./prendas";

urls.forEach((url) => {
    // Extraer la parte de la ruta después del bucket
    const urlObj = new URL(url);
    const rutaRelativa = urlObj.pathname.replace("/expo/marcas/", ""); // quitamos la parte fija que no queremos
    const fullPath = path.join(baseDir, rutaRelativa);

    // Crear carpetas necesarias
    const dir = path.dirname(fullPath);
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
    }

    // Descargar la imagen
    const file = fs.createWriteStream(fullPath);
    https.get(url, (response) => {
        response.pipe(file);
        file.on("finish", () => {
            file.close();
            console.log(`✅ Descargada: ${fullPath}`);
        });
    }).on("error", (err) => {
        console.error(`❌ Error descargando ${url}: ${err.message}`);
    });
});
