package xj.property.utils.image.utils;

import java.io.File;

public class FileManager
{

	public static String getSaveFilePath()
	{
		if (CommonUtil.hasSDCard())
		{
			String topPath = CommonUtil.getRootFilePath() + "zzw/";
			File topDir = new File(topPath);
			if (!topDir.exists())
			{
				topDir.mkdirs();
			}
			String subPath = topDir.getPath() + "/" + "cache/";
			File subDir = new File(subPath);
			if (!subDir.exists())
			{
				subDir.mkdirs();
			}
			return subDir.getPath();
			// return CommonUtil.getRootFilePath() + "zzw/cache/";
		} else
		{
			String topPath = CommonUtil.getRootFilePath() + "zzw/";
			File topDir = new File(topPath);
			if (!topDir.exists())
			{
				topDir.mkdirs();
			}
			String subPath = topDir.getPath() + "/" + "cache/";
			File subDir = new File(subPath);
			if (!subDir.exists())
			{
				subDir.mkdirs();
			}
			return subDir.getPath();
			// return CommonUtil.getRootFilePath() + "zzw/cache/";
		}
	}

}
