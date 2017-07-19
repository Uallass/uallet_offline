package com.example.uallas.uallet.lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.widget.PopupWindow;

public class PopupHandler {
	
	/**
	 * Calcula o tamanho da popup de Modelos de Veículos de acordo com o tamanho
	 * da tela.
	 * 
	 * @param activity
	 *            activity onde a popup será aberta
	 * @param widthPorcentagem
	 *            comprimento da popup em relação ao comprimento total da tela
	 *            (porcentagem). Ex: 50 equivale à 50% do comprimento total da
	 *            tela.
	 * @param heightPorcentagem
	 *            altura da popup em relação à altura total da tela
	 *            (porcentagem). Ex: 50 equivale à 50% da altura total da tela.
	 * @author thales.valias
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void definePoputSize(Activity activity,
			int widthPorcentagem, int heightPorcentagem, PopupWindow popupWindow) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		
		int screenWidth;
		int screenHeight;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
			screenHeight = size.y;
		} else {
			screenWidth = display.getWidth();
			screenHeight = display.getHeight();
		}
		
		float width = (float) widthPorcentagem / (float) 100 * screenWidth;
		float height = (float) heightPorcentagem / (float) 100 * screenHeight;
		
		popupWindow.update((int) width,(int) height);
	}
	
}
