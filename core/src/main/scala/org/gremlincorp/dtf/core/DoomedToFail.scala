package org.gremlincorp.dtf.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.Input.Keys
import scala.math.{min, max}
import com.badlogic.gdx.audio.Music

class DoomedToFail extends ApplicationListener {
    val viewWidth = 800f / 2f
    val viewHeight = 480f / 2f
    val moveSpeed = 200

    var camera : OrthographicCamera = _
    var spriteImage : Texture  = _
    var spriteLoc : Rectangle = _
    var batch : SpriteBatch  = _
    var renderer : OrthogonalTiledMapRenderer = _
    var bgm : Music = _

    override def create = {
        val w = Gdx.graphics.getWidth()
        val h = Gdx.graphics.getHeight()

        this.camera = new OrthographicCamera()
        this.camera.setToOrtho(false, viewWidth, viewHeight)
        this.camera.update()

        loadSprite()

        this.bgm = Gdx.audio.newMusic(Gdx.files.internal("Tryad - All the Same.mp3"))
        this.bgm.setLooping(true)
        this.bgm.play()

        this.batch = new SpriteBatch()

        val map = getMap()
        this.renderer = new OrthogonalTiledMapRenderer(map)
    }

    def getMap() : TiledMap  = {
        val assetManager = new AssetManager()
        assetManager.setLoader(classOf[TiledMap], new TmxMapLoader(new InternalFileHandleResolver()))
        assetManager.load("plains.tmx", classOf[TiledMap])
        assetManager.finishLoading()
        return assetManager.get("plains.tmx")
    }

    def loadSprite() : Unit = {
        this.spriteImage = new Texture(Gdx.files.internal("head.png"))
        this.spriteLoc = new Rectangle
        this.spriteLoc.x = 100
        this.spriteLoc.y = 100
        this.spriteLoc.width = 20
        this.spriteLoc.height = 28
    }

    override def render () = {
        Gdx.gl.glClearColor(0, 0, 0, 0)
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

        this.camera.update()
        this.renderer.setView(this.camera)
        this.renderer.render()

        this.batch.setProjectionMatrix(camera.combined)
        this.batch.begin()
        move()
        this.batch.draw(this.spriteImage, this.spriteLoc.x, this.spriteLoc.y)
        this.batch.end()
    }

    def move () : Unit = {
        val deltaTime = Gdx.graphics.getDeltaTime()

        if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            spriteLoc.x = max(0, spriteLoc.x - moveSpeed * deltaTime)
        }

        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            spriteLoc.x = min(viewWidth - spriteLoc.width, spriteLoc.x + moveSpeed * deltaTime)
        }

        if(Gdx.input.isKeyPressed(Keys.DOWN)) {
            spriteLoc.y = max(0, spriteLoc.y - moveSpeed * deltaTime)
        }

        if(Gdx.input.isKeyPressed(Keys.UP)) {
            spriteLoc.y = min(viewHeight - spriteLoc.height, spriteLoc.y + moveSpeed * deltaTime)
        }
    }

    override def resize (width : Int, height : Int) = {}
    override def pause () = {}
    override def resume () = {}
    override def dispose () = {
        this.spriteImage.dispose()
        this.bgm.dispose()
        this.batch.dispose()
    }
}