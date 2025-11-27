package com.outfitlab.project.domain.model.dto;

import com.outfitlab.project.domain.model.BrandModel;
import com.outfitlab.project.domain.model.ClimaModel;
import com.outfitlab.project.domain.model.ColorModel;
import com.outfitlab.project.domain.model.PrendaModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GarmentDTOTest {

    // ========== CONSTRUCTOR TESTS ==========

    @Test
    void givenValidDataWhenCreateWithSevenParamsThenAllFieldsAreSet() {
        // GIVEN
        String nombre = "Nike Shirt";
        String tipo = "superior";
        String imagenUrl = "http://image.url/shirt.jpg";
        String garmentCode = "SHIRT-001";
        String marcaNombre = "Nike";
        String color = "Rojo";
        String clima = "Calido";

        // WHEN
        GarmentDTO dto = new GarmentDTO(nombre, tipo, imagenUrl, garmentCode, marcaNombre, color, clima);

        // THEN
        assertEquals(nombre, dto.getNombre());
        assertEquals(tipo, dto.getTipo());
        assertEquals(imagenUrl, dto.getImagenUrl());
        assertEquals(garmentCode, dto.getGarmentCode());
        assertEquals(marcaNombre, dto.getMarcaNombre());
        assertEquals(color, dto.getColor());
        assertEquals(clima, dto.getClima());
        assertNull(dto.getGenero()); // Not set in 7-param constructor
        assertNull(dto.getEvento()); // Not set in constructor
    }

    @Test
    void givenValidDataWhenCreateWithEightParamsThenAllFieldsAreSet() {
        // GIVEN
        String nombre = "Adidas Pants";
        String tipo = "inferior";
        String imagenUrl = "http://image.url/pants.jpg";
        String garmentCode = "PANTS-001";
        String marcaNombre = "Adidas";
        String color = "Azul";
        String clima = "Frio";
        String genero = "Masculino";

        // WHEN
        GarmentDTO dto = new GarmentDTO(nombre, tipo, imagenUrl, garmentCode, marcaNombre, color, clima, genero);

        // THEN
        assertEquals(nombre, dto.getNombre());
        assertEquals(tipo, dto.getTipo());
        assertEquals(imagenUrl, dto.getImagenUrl());
        assertEquals(garmentCode, dto.getGarmentCode());
        assertEquals(marcaNombre, dto.getMarcaNombre());
        assertEquals(color, dto.getColor());
        assertEquals(clima, dto.getClima());
        assertEquals(genero, dto.getGenero());
        assertNull(dto.getEvento()); // Not set in constructor
    }

    // ========== CONVERSION TESTS ==========

    @Test
    void givenValidPrendaModelWhenConvertToDtoThenAllFieldsAreMapped() {
        // GIVEN
        PrendaModel garment = givenValidPrendaModel();

        // WHEN
        GarmentDTO dto = GarmentDTO.convertModelToDTO(garment);

        // THEN
        thenDtoHasCorrectFields(dto, garment);
    }

    @Test
    void givenPrendaModelWithNullBrandWhenConvertToDtoThenThrowNullPointerException() {
        // GIVEN
        PrendaModel garment = givenPrendaModelWithNullBrand();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> GarmentDTO.convertModelToDTO(garment),
                "Should throw NullPointerException when brand is null");
    }

    @Test
    void givenPrendaModelWithNullColorWhenConvertToDtoThenThrowNullPointerException() {
        // GIVEN
        PrendaModel garment = givenPrendaModelWithNullColor();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> GarmentDTO.convertModelToDTO(garment),
                "Should throw NullPointerException when color is null");
    }

    @Test
    void givenPrendaModelWithNullClimaWhenConvertToDtoThenThrowNullPointerException() {
        // GIVEN
        PrendaModel garment = givenPrendaModelWithNullClima();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> GarmentDTO.convertModelToDTO(garment),
                "Should throw NullPointerException when clima is null");
    }

    // ========== SETTER/GETTER TESTS ==========

    @Test
    void givenDtoWhenSetNombreThenGetNombreReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newNombre = "Updated Name";

        // WHEN
        dto.setNombre(newNombre);

        // THEN
        assertEquals(newNombre, dto.getNombre());
    }

    @Test
    void givenDtoWhenSetTipoThenGetTipoReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newTipo = "calzado";

        // WHEN
        dto.setTipo(newTipo);

        // THEN
        assertEquals(newTipo, dto.getTipo());
    }

    @Test
    void givenDtoWhenSetImagenUrlThenGetImagenUrlReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newUrl = "http://new-image.url/garment.jpg";

        // WHEN
        dto.setImagenUrl(newUrl);

        // THEN
        assertEquals(newUrl, dto.getImagenUrl());
    }

    @Test
    void givenDtoWhenSetGarmentCodeThenGetGarmentCodeReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newCode = "NEW-CODE-123";

        // WHEN
        dto.setGarmentCode(newCode);

        // THEN
        assertEquals(newCode, dto.getGarmentCode());
    }

    @Test
    void givenDtoWhenSetMarcaNombreThenGetMarcaNombreReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newMarca = "Puma";

        // WHEN
        dto.setMarcaNombre(newMarca);

        // THEN
        assertEquals(newMarca, dto.getMarcaNombre());
    }

    @Test
    void givenDtoWhenSetEventoThenGetEventoReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String evento = "Formal";

        // WHEN
        dto.setEvento(evento);

        // THEN
        assertEquals(evento, dto.getEvento());
    }

    @Test
    void givenDtoWhenSetColorThenGetColorReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newColor = "Verde";

        // WHEN
        dto.setColor(newColor);

        // THEN
        assertEquals(newColor, dto.getColor());
    }

    @Test
    void givenDtoWhenSetClimaThenGetClimaReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String newClima = "Templado";

        // WHEN
        dto.setClima(newClima);

        // THEN
        assertEquals(newClima, dto.getClima());
    }

    @Test
    void givenDtoWhenSetGeneroThenGetGeneroReturnsCorrectValue() {
        // GIVEN
        GarmentDTO dto = givenBasicGarmentDTO();
        String genero = "Femenino";

        // WHEN
        dto.setGenero(genero);

        // THEN
        assertEquals(genero, dto.getGenero());
    }

    // ========== TO_STRING TEST ==========

    @Test
    void givenDtoWhenToStringThenContainsAllFields() {
        // GIVEN
        GarmentDTO dto = new GarmentDTO("Test", "superior", "http://test.jpg", "TEST-001", "TestBrand", "Rojo",
                "Calido", "Masculino");
        dto.setEvento("Casual");

        // WHEN
        String result = dto.toString();

        // THEN
        assertTrue(result.contains("nombre='Test'"));
        assertTrue(result.contains("tipo='superior'"));
        assertTrue(result.contains("imagenUrl='http://test.jpg'"));
        assertTrue(result.contains("garmentCode='TEST-001'"));
        assertTrue(result.contains("marcaNombre='TestBrand'"));
        assertTrue(result.contains("evento='Casual'"));
        assertTrue(result.contains("color='Rojo'"));
        assertTrue(result.contains("clima='Calido'"));
        assertTrue(result.contains("genero='Masculino'"));
    }

    // ========== HELPER METHODS (GIVEN) ==========

    private PrendaModel givenValidPrendaModel() {
        PrendaModel model = new PrendaModel();
        model.setNombre("Nike Shirt");
        model.setTipo("superior");
        model.setImagenUrl("http://image.url/shirt.jpg");
        model.setGarmentCode("SHIRT-001");
        model.setGenero("Masculino");

        BrandModel brand = new BrandModel();
        brand.setNombre("Nike");
        model.setMarca(brand);

        ColorModel color = new ColorModel();
        color.setNombre("Rojo");
        model.setColor(color);

        ClimaModel clima = new ClimaModel();
        clima.setNombre("Calido");
        model.setClimaAdecuado(clima);

        return model;
    }

    private PrendaModel givenPrendaModelWithNullBrand() {
        PrendaModel model = new PrendaModel();
        model.setNombre("Test");
        model.setTipo("superior");
        model.setImagenUrl("http://test.jpg");
        model.setGarmentCode("TEST-001");
        model.setGenero("Masculino");
        model.setMarca(null); // NULL BRAND

        ColorModel color = new ColorModel();
        color.setNombre("Rojo");
        model.setColor(color);

        ClimaModel clima = new ClimaModel();
        clima.setNombre("Calido");
        model.setClimaAdecuado(clima);

        return model;
    }

    private PrendaModel givenPrendaModelWithNullColor() {
        PrendaModel model = new PrendaModel();
        model.setNombre("Test");
        model.setTipo("superior");
        model.setImagenUrl("http://test.jpg");
        model.setGarmentCode("TEST-001");
        model.setGenero("Masculino");

        BrandModel brand = new BrandModel();
        brand.setNombre("Nike");
        model.setMarca(brand);

        model.setColor(null); // NULL COLOR

        ClimaModel clima = new ClimaModel();
        clima.setNombre("Calido");
        model.setClimaAdecuado(clima);

        return model;
    }

    private PrendaModel givenPrendaModelWithNullClima() {
        PrendaModel model = new PrendaModel();
        model.setNombre("Test");
        model.setTipo("superior");
        model.setImagenUrl("http://test.jpg");
        model.setGarmentCode("TEST-001");
        model.setGenero("Masculino");

        BrandModel brand = new BrandModel();
        brand.setNombre("Nike");
        model.setMarca(brand);

        ColorModel color = new ColorModel();
        color.setNombre("Rojo");
        model.setColor(color);

        model.setClimaAdecuado(null); // NULL CLIMA

        return model;
    }

    private GarmentDTO givenBasicGarmentDTO() {
        return new GarmentDTO("Test", "superior", "http://test.jpg", "TEST-001", "TestBrand", "Rojo", "Calido");
    }

    // ========== HELPER METHODS (THEN) ==========

    private void thenDtoHasCorrectFields(GarmentDTO dto, PrendaModel model) {
        assertEquals(model.getNombre(), dto.getNombre(), "Nombre should match");
        assertEquals(model.getTipo(), dto.getTipo(), "Tipo should match");
        assertEquals(model.getImagenUrl(), dto.getImagenUrl(), "ImagenUrl should match");
        assertEquals(model.getGarmentCode(), dto.getGarmentCode(), "GarmentCode should match");
        assertEquals(model.getMarca().getNombre(), dto.getMarcaNombre(), "MarcaNombre should match");
        assertEquals(model.getColor().getNombre(), dto.getColor(), "Color should match");
        assertEquals(model.getClimaAdecuado().getNombre(), dto.getClima(), "Clima should match");
        assertEquals(model.getGenero(), dto.getGenero(), "Genero should match");
    }
}
