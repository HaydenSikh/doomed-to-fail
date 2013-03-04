package org.gremlincorp.dtf.core

import scala.math.max
import scala.math.min

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx.audio
import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle

object DoomedToFail {
    val viewWidth = 800
    val viewHeight = 480
    val spriteWidth = 21
    val spriteHeight = 35
}

class DoomedToFail extends ApplicationListener {
    import DoomedToFail._

    val moveSpeed = 150  // pixels per second

    private var camera: OrthographicCamera = _
    //private var spriteImage : Texture  = _
    private var spriteLoc: Rectangle = _
    private var batch: SpriteBatch = _
    private var renderer: OrthogonalTiledMapRenderer = _
    private var font: BitmapFont = _
    private var bgm: Music = _

    private var walkSheet: Texture = _
    private var walkFrames: Array[TextureRegion] = _
    private var walkAnimation: Animation = _
    private var stateTime = 0f

    override def create = {
        camera = new OrthographicCamera()
        camera.setToOrtho(false, viewWidth, viewHeight)
        camera.update()

        loadSprite()

        font = new BitmapFont

        bgm = audio.newMusic(files.internal("Tryad - All the Same.mp3"))
        bgm.setLooping(true)
        bgm.play()

        batch = new SpriteBatch()

        val map = getMap()
        renderer = new OrthogonalTiledMapRenderer(map)
    }

    def getMap(): TiledMap = {
        val assetManager = new AssetManager()
        assetManager.setLoader(classOf[TiledMap], new TmxMapLoader(new InternalFileHandleResolver()))
        assetManager.load("plains.tmx", classOf[TiledMap])
        assetManager.finishLoading()
        return assetManager.get("plains.tmx")
    }

    def loadSprite(): Unit = {
        // spriteImage = new Texture(files.internal("head.png"))
        // width, height of 20, 28 for head.png
        walkSheet = new Texture(files.internal("Victor_Walk.png"))
        walkFrames = TextureRegion.split(walkSheet, spriteWidth, spriteHeight).flatten

        walkAnimation = new Animation(0.15f, walkFrames: _*)

        spriteLoc = new Rectangle
        spriteLoc.x = 100
        spriteLoc.y = 100
        spriteLoc.width = spriteWidth
        spriteLoc.height = spriteHeight
    }

    override def render() = {
        gl.glClearColor(0, 0, 0, 0)
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

        camera.update()
        renderer.setView(camera)
        renderer.render()

        batch.setProjectionMatrix(camera.combined)
        batch.begin()
        font.draw(batch, "Doomed to Fail", 0, 15)
        move()
        val currentFrame = walkAnimation.getKeyFrame(stateTime, true)
        batch.draw(currentFrame, spriteLoc.x, spriteLoc.y)
        batch.end()
    }

    def move(): Unit = {
        val deltaTime = graphics.getDeltaTime()
        val isMovingLeft = input.isKeyPressed(Keys.LEFT) && !input.isKeyPressed(Keys.RIGHT)
        val isMovingRight = input.isKeyPressed(Keys.RIGHT) && !input.isKeyPressed(Keys.LEFT)
        val isMovingUp = input.isKeyPressed(Keys.UP) && !input.isKeyPressed(Keys.DOWN)
        val isMovingDown = input.isKeyPressed(Keys.DOWN) && !input.isKeyPressed(Keys.UP)
        var isStanding = !isMovingLeft && !isMovingRight && !isMovingUp && !isMovingDown

        if (isMovingLeft) {
            spriteLoc.x = max(0, spriteLoc.x - moveSpeed * deltaTime)
        } else if (isMovingRight) {
            spriteLoc.x = min(DoomedToFail.viewWidth - spriteLoc.width, spriteLoc.x + moveSpeed * deltaTime)
        }

        if (isMovingDown) {
            spriteLoc.y = max(0, spriteLoc.y - moveSpeed * deltaTime)
        } else if (isMovingUp) {
            spriteLoc.y = min(DoomedToFail.viewHeight - spriteLoc.height, spriteLoc.y + moveSpeed * deltaTime)
        }

        stateTime = if (isStanding) 0f else stateTime + deltaTime
    }

    override def resize(width: Int, height: Int) = {}

    override def pause() = {}

    override def resume() = {}

    override def dispose() = {
        walkSheet.dispose()
        bgm.dispose()
        batch.dispose()
        font.dispose()
    }
}