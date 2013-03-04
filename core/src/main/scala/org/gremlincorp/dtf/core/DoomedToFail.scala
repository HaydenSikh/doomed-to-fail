package org.gremlincorp.dtf.core

import scala.math.max
import scala.math.min
import scala.collection.JavaConverters._
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx.audio
import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Gdx.app
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
import com.badlogic.gdx.maps.MapProperties
import scala.collection.mutable.StringBuilder

object DoomedToFail {
    val viewWidth = 800
    val viewHeight = 480
    val spriteWidth = 21
    val spriteHeight = 35
    val logTag = "DoomedToFail"
}

class DoomedToFail extends ApplicationListener {
    import DoomedToFail._

    val moveSpeed = 150  // pixels per second

    private var camera: OrthographicCamera = _
    private var spriteLoc: Rectangle = _
    private var batch: SpriteBatch = _
    private var font: BitmapFont = _

    private var renderer: OrthogonalTiledMapRenderer = _
    private var map: TiledMap = _
    private var mapWidth: Int = _
    private var mapHeight: Int = _

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

        batch = new SpriteBatch()

        map = getMap()
        renderer = new OrthogonalTiledMapRenderer(map)
    }

    def getMap(): TiledMap = {
        val assetManager = new AssetManager()
        assetManager.setLoader(classOf[TiledMap], new TmxMapLoader(new InternalFileHandleResolver()))
        assetManager.load("plains.tmx", classOf[TiledMap])
        assetManager.finishLoading()

        val map: TiledMap = assetManager.get("plains.tmx")
        val properties = map.getProperties

        implicit def toString: (MapProperties)=>String = { properties =>
            val sb = new StringBuilder("[")
            properties.getKeys.asScala.foreach { k =>
                sb.append("[" + k + "=" + properties.get(k) + "],")
            }
            sb.deleteCharAt(sb.length - 1)
            sb.append("]")
            sb.toString
        }

        app.log(logTag, properties)
        mapWidth = properties.get("width", classOf[Int]) * properties.get("tilewidth", classOf[Int])
        mapHeight = properties.get("height", classOf[Int]) * properties.get("tileheight", classOf[Int])

        return map
    }

    def loadSprite(): Unit = {
        walkSheet = new Texture(files.internal("Victor_Walk.png"))
        walkFrames = TextureRegion.split(walkSheet, spriteWidth, spriteHeight).flatten

        walkAnimation = new Animation(0.15f, walkFrames: _*)

        spriteLoc = new Rectangle
        spriteLoc.x = (viewWidth/2f) - (spriteWidth/2f)
        spriteLoc.y = (viewHeight/2f) - (spriteHeight/2f)
        spriteLoc.width = spriteWidth
        spriteLoc.height = spriteHeight
    }

    override def render() = {
        gl.glClearColor(0, 0, 0, 0)
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

        move()
        camera.update()
        renderer.setView(camera)
        renderer.render()

        batch.begin()
        batch.setProjectionMatrix(camera.combined)
        font.draw(batch, "Doomed to Fail", 0, 15)
        val currentFrame = walkAnimation.getKeyFrame(stateTime, true)
        batch.draw(currentFrame, spriteLoc.x, spriteLoc.y)
        batch.end()
    }

    def move(): Unit = {
        val deltaTime = graphics.getDeltaTime()
        val moveAmount = moveSpeed * deltaTime

        val isMovingLeft = input.isKeyPressed(Keys.LEFT) && !input.isKeyPressed(Keys.RIGHT)
        val isMovingRight = input.isKeyPressed(Keys.RIGHT) && !input.isKeyPressed(Keys.LEFT)
        val isMovingUp = input.isKeyPressed(Keys.UP) && !input.isKeyPressed(Keys.DOWN)
        val isMovingDown = input.isKeyPressed(Keys.DOWN) && !input.isKeyPressed(Keys.UP)
        val isStanding = !isMovingLeft && !isMovingRight && !isMovingUp && !isMovingDown

        if (isMovingLeft) {
            spriteLoc.x = max(0, spriteLoc.x - moveAmount)
            if (spriteLoc.x <= mapWidth - viewWidth/2f) {
                camera.translate(max(0 - (camera.position.x - viewWidth/2f), -moveAmount), 0)
            }
        } else if (isMovingRight) {
            renderer.getViewBounds()
            spriteLoc.x = min(mapWidth - spriteLoc.width, spriteLoc.x + moveAmount)
            if (spriteLoc.x >= viewWidth/2f) {
                camera.translate(min(mapWidth - (camera.position.x + viewWidth/2f), moveAmount), 0)
            }
        }

        if (isMovingDown) {
            spriteLoc.y = max(0, spriteLoc.y - moveAmount)

            if (spriteLoc.y <= mapHeight - viewHeight/2f) {
                camera.translate(0, max(0 - (camera.position.y - viewHeight/2f), -moveAmount))
            }
        } else if (isMovingUp) {
            spriteLoc.y = min(mapHeight - spriteLoc.height, spriteLoc.y + moveAmount)
            if (spriteLoc.y >= viewHeight/2f) {
                camera.translate(0, min(mapHeight - (camera.position.y + viewHeight/2f), moveAmount))
            }
        }

        stateTime = if (isStanding) 0f else stateTime + deltaTime
    }

    override def resize(width: Int, height: Int) = {}

    override def pause() = {}

    override def resume() = {}

    override def dispose() = {
        map.dispose()
        walkSheet.dispose()
        batch.dispose()
        font.dispose()
    }
}