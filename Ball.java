import java.awt.*;
import java.util.Random;

/**
 * Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
 * instancia um objeto deste tipo quando a execução é iniciada.
 */

public class Ball {

	/**
	 * Construtor da classe Ball. Observe que quem invoca o construtor desta classe
	 * define a velocidade da bola
	 * (em pixels por millisegundo), mas não define a direção deste movimento. A
	 * direção do movimento é determinada
	 * aleatóriamente pelo construtor.
	 *
	 * @param cx     coordenada x da posição inicial da bola (centro do retangulo
	 *               que a representa).
	 * @param cy     coordenada y da posição inicial da bola (centro do retangulo
	 *               que a representa).
	 * @param width  largura do retangulo que representa a bola.
	 * @param height altura do retangulo que representa a bola.
	 * @param color  cor da bola.
	 * @param speed  velocidade da bola (em pixels por millisegundo).
	 */

	private double cx;
	private double cy;
	private double width;
	private double height;
	private Color color;
	private double speed;
	// direção da bola na horizontal
	private double dx;
	// direção da bola na vertical
	private double dy;

	public Ball(double cx, double cy, double width, double height, Color color, double speed) {

		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.color = color;
		this.speed = speed;

		// Define a direção da bola aleatóriamente
		Random random = new Random();
		// se for 1, a bola vai para a direita ou para cima
		// se for -1, a bola vai para a esquerda ou para baixo
		this.dx = random.nextInt(2) == 0 ? 1 : -1;
		this.dy = random.nextInt(2) == 0 ? 1 : -1;
	}

	/**
	 * Método chamado sempre que a bola precisa ser (re)desenhada.
	 */

	public void draw() {

		GameLib.setColor(color);
		GameLib.fillRect(cx, cy, width, height);
	}

	/**
	 * Método chamado quando o estado (posição) da bola precisa ser atualizado.
	 *
	 * @param delta quantidade de millisegundos que se passou entre o ciclo anterior
	 *              de atualização do jogo e o atual.
	 */

	public void update(long delta) {

		this.cx = this.cx + this.dx * delta;
		this.cy = this.cy + this.dy * delta;

	}

	/**
	 * Método chamado quando detecta-se uma colisão da bola com um jogador.
	 *
	 * @param playerId uma string cujo conteúdo identifica um dos jogadores.
	 */

	public void onPlayerCollision(String playerId) {

		// se a bola estiver indo para a esquerda, ela vai para a direita
		double positiveSpeed = (speed < 0) ? -this.speed : this.speed;

		if (playerId.equals("Player 1")) {
			// se a bola estiver indo para a esquerda, ela vai para a direita
			this.dx = positiveSpeed;
		} else {
			// não era para precisar inverter, mas deu certo
			this.dx = -positiveSpeed;
		}
	}

	/**
	 * Método chamado quando detecta-se uma colisão da bola com uma parede.
	 *
	 * @param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	 */

	public void onWallCollision(String wallId) {

		double positiveSpeed = (speed < 0) ? -speed : speed;

		switch (wallId) {
			case Pong.TOP:
				this.dy = positiveSpeed;
				break;
			case Pong.LEFT:
				this.dx = positiveSpeed;
				break;
			case Pong.RIGHT:
				this.dx = -positiveSpeed;
				break;
			case Pong.BOTTOM:
				this.dy = -positiveSpeed;
				break;
		}
	}

	/**
	 * Método que verifica se houve colisão da bola com uma parede.
	 *
	 * @param wall referência para uma instância de Wall contra a qual será
	 *             verificada a ocorrência de colisão da bola.
	 * @return um valor booleano que indica a ocorrência (true) ou não (false) de
	 *         colisão.
	 */

	public boolean checkCollision(Wall wall) {
		String id = wall.getId();

		switch (id) {
			case Pong.TOP:
				if (this.cy - (this.height / 2) <= wall.getCy() + (wall.getHeight() / 2))
					return true;
				break;
			case Pong.BOTTOM:
				if (this.cy + (this.height / 2) >= wall.getCy() - (wall.getHeight() / 2))
					return true;
				break;
			case Pong.LEFT:
				if (this.cx - (this.width / 2) <= wall.getCx() + (wall.getWidth() / 2))
					return true;
				break;
			case Pong.RIGHT:
				if (this.cx + (this.width / 2) >= wall.getCx() - (wall.getWidth() / 2))
					return true;
				break;
		}

		return false;
	}

	/**
	 * Método que verifica se houve colisão da bola com um jogador.
	 *
	 * @param player referência para uma instância de Player contra o qual será
	 *               verificada a ocorrência de colisão da bola.
	 * @return um valor booleano que indica a ocorrência (true) ou não (false) de
	 *         colisão.
	 */

	public boolean checkCollision(Player player) {
		// Verifica se a bola está dentro da área do jogador
		boolean ballIsLeftOfPlayer = cx - (width / 2) < player.getCx() + (player.getWidth() / 2);
		boolean ballIsRightOfPlayer = cx + (width / 2) > player.getCx() - (player.getWidth() / 2);
		boolean ballIsAbovePlayer = cy - (height / 2) < player.getCy() + (player.getHeight() / 2);
		boolean ballIsBelowPlayer = cy + (height / 2) > player.getCy() - (player.getHeight() / 2);

		boolean isInsidePlayer = ballIsLeftOfPlayer && ballIsRightOfPlayer && ballIsAbovePlayer && ballIsBelowPlayer;

		// Verifica se o jogador é o PLAYER1 ou PLAYER2
		boolean isPlayer1OrPlayer2 = player.getId().equals(Pong.PLAYER1) || player.getId().equals(Pong.PLAYER2);

		// Retorna true se a bola está dentro da área do jogador e o jogador é o PLAYER1 ou PLAYER2
		return isInsidePlayer && isPlayer1OrPlayer2;
	}

	/**
	 * Método que devolve a coordenada x do centro do retângulo que representa a
	 * bola.
	 *
	 * @return o valor double da coordenada x.
	 */

	public double getCx() {

		return this.cx;
	}

	/**
	 * Método que devolve a coordenada y do centro do retângulo que representa a
	 * bola.
	 *
	 * @return o valor double da coordenada y.
	 */

	public double getCy() {

		return this.cy;
	}

	/**
	 * Método que devolve a velocidade da bola.
	 *
	 * @return o valor double da velocidade.
	 *
	 */

	public double getSpeed() {

		return this.speed;
	}

}