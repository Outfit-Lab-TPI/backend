import fs from "fs";
import path from "path";

// Lista de logos
const logos = [
    {
        "codigoMarca": "puma",
        "nombre": "Puma",
        "logoUrl": "C:\\Users\\germa\\OneDrive\\Escritorio\\backend\\src\\main\\resources\\img\\logos\\puma.png",
        "sitioUrl": "https://www.puma.com"
    },
    {
        "codigoMarca": "vulk",
        "nombre": "Vulk",
        "logoUrl": "C:\\Users\\germa\\OneDrive\\Escritorio\\backend\\src\\main\\resources\\img\\logos\\vulk.webp",
        "sitioUrl": "https://vulkclothing.com/"
    },
    {
        "codigoMarca": "volcom",
        "nombre": "Volcom",
        "logoUrl": "C:\\Users\\germa\\OneDrive\\Escritorio\\backend\\src\\main\\resources\\img\\logos\\volcom.webp",
        "sitioUrl": "https://www.volcom.com.ar/"
    }
];

// Carpeta destino
const baseDir = "./prendas/logos";

// Crear carpeta si no existe
if (!fs.existsSync(baseDir)) {
    fs.mkdirSync(baseDir, { recursive: true });
}

// Copiar cada logo
logos.forEach(logo => {
    const nombreArchivo = path.basename(logo.logoUrl);
    const destino = path.join(baseDir, nombreArchivo);

    fs.copyFile(logo.logoUrl, destino, (err) => {
        if (err) {
            console.error(`❌ Error copiando ${logo.logoUrl}: ${err.message}`);
        } else {
            console.log(`✅ Copiado: ${destino}`);
        }
    });
});
