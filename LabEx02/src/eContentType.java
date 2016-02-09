
public enum eContentType {
	text,
	image_jpg,
	image_gif,
	image_bmp,
	image_png,
	icon,
	application;
	
	public String GetType() {
		String toReturn;
		switch (this) {
		case text:
			toReturn = "text/html";
			break;
		case image_jpg:
			toReturn = "image/jpg";
			break;
		case image_gif:
			toReturn = "image/gif";
			break;
		case image_bmp:
			toReturn = "image/bmp";
			break;
		case image_png:
			toReturn = "image/png";
			break;
		case icon:
			toReturn = "icon";
			break;
		case application:
			toReturn = "application/octet-stream";
			break;
		default:
			toReturn = "";
			break;
		}
		
		return toReturn;
	}
}
