package main;

import entity.Entity;

public class CollisionChecker {
    PanelGry pg;

    public CollisionChecker(PanelGry pg) {
        this.pg = pg;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.x + entity.solidArea.x;
        int entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.y + entity.solidArea.y;
        int entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / pg.tileSize;
        int entityRightCol = entityRightWorldX / pg.tileSize;
        int entityTopRow = entityTopWorldY / pg.tileSize;
        int entityBottomRow = entityBottomWorldY / pg.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / pg.tileSize;
                tileNum1 = pg.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = pg.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (pg.tileM.tile[tileNum1].collision || pg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / pg.tileSize;
                tileNum1 = pg.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = pg.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (pg.tileM.tile[tileNum1].collision || pg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / pg.tileSize;
                tileNum1 = pg.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = pg.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (pg.tileM.tile[tileNum1].collision || pg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / pg.tileSize;
                tileNum1 = pg.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = pg.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (pg.tileM.tile[tileNum1].collision || pg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}
