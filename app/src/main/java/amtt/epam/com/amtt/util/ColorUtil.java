package amtt.epam.com.amtt.util;

import android.graphics.Color;

/**
 * Created on 4/27/2015.
 *
 * based on https://github.com/rey5137/material/blob/master/lib/src/main/java/com/rey/material/util/ColorUtil.java
 */
public class ColorUtil {

	public static int getColorWithAlpha(int baseColor, float alphaPercent){
		int alpha = Math.round(Color.alpha(baseColor) * alphaPercent);
		
		return (baseColor & 0x00FFFFFF) | (alpha << 24);
	}
}
