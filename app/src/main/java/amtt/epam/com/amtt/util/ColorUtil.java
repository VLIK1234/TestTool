package amtt.epam.com.amtt.util;

import android.graphics.Color;

/**
 * Created by Iryna_Monchanka on 4/27/2015.
 */
public class ColorUtil {

	public static int getColor(int baseColor, float alphaPercent){				
		int alpha = Math.round(Color.alpha(baseColor) * alphaPercent);
		
		return (baseColor & 0x00FFFFFF) | (alpha << 24);
	}
}
