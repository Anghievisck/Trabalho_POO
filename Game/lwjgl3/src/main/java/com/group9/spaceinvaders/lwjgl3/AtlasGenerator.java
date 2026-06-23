package com.group9.spaceinvaders.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AtlasGenerator {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        // Caminho de onde ele vai LER as imagens soltas
        String inputDir = "Game/raw_assets/sprites"; 
        
        // Caminho de onde ele vai SALVAR o Atlas pronto
        String outputDir = "Game/assets/sprites"; 
        
        String packFileName = "gameplay"; 

        TexturePacker.process(settings, inputDir, outputDir, packFileName);
        System.out.println("Atlas gerado com sucesso na pasta assets/sprites!");
    }
}