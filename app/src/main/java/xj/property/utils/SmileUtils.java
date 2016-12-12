/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xj.property.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xj.property.R;
import xj.property.widget.VerticalImageSpan;


public class SmileUtils {


    public static final String ee_1 = "[微笑]";
    public static final String ee_2 = "[嘻嘻]";
    public static final String ee_3 = "[哈哈]";
    public static final String ee_4 = "[爱你]";
    public static final String ee_5 = "[挖鼻]";
    public static final String ee_6 = "[吃惊]";
    public static final String ee_7 = "[晕]";
    public static final String ee_8 = "[泪]";
    public static final String ee_9 = "[馋嘴]";
    public static final String ee_10 = "[抓狂]";
    public static final String ee_11 = "[哼]";
    public static final String ee_12 = "[可爱]";
    public static final String ee_13 = "[怒]";
    public static final String ee_14 = "[汗]";
    public static final String ee_15 = "[害羞]";
    public static final String ee_16 = "[钱]";
    public static final String ee_17 = "[偷笑]";
    public static final String ee_18 = "[笑cry]";
    public static final String ee_19 = "[睡]";
    public static final String ee_20 = "[互粉]";
    public static final String ee_21 = "[傻眼]";
    public static final String ee_22 = "[酷]";
    public static final String ee_23 = "[衰]";
    public static final String ee_24 = "[闭嘴]";
    public static final String ee_25 = "[鄙视]";
    public static final String ee_26 = "[色]";
    public static final String ee_27 = "[鼓掌]";
    public static final String ee_28 = "[悲伤]";
    public static final String ee_29 = "[思考]";
    public static final String ee_30 = "[生病]";
    public static final String ee_31 = "[亲亲]";
    public static final String ee_32 = "[怒骂]";
    public static final String ee_33 = "[可怜]";
    public static final String ee_34 = "[太开心]";
    public static final String ee_35 = "[白眼]";
    public static final String ee_36 = "[右哼哼]";
    public static final String ee_37 = "[左哼哼]";
    public static final String ee_38 = "[嘘]";
    public static final String ee_39 = "[委屈]";
    public static final String ee_40 = "[吐]";
    public static final String ee_41 = "[挤眼]";
    public static final String ee_42 = "[哈欠]";
    public static final String ee_43 = "[失望]";
    public static final String ee_44 = "[顶]";
    public static final String ee_45 = "[疑问]";
    public static final String ee_46 = "[困]";
    public static final String ee_47 = "[感冒]";
    public static final String ee_48 = "[拜拜]";
    public static final String ee_49 = "[黑线]";
    public static final String ee_50 = "[阴险]";
    public static final String ee_51 = "[打脸]";
    public static final String ee_52 = "[喵喵]";
    public static final String ee_53 = "[doge]";
    public static final String ee_54 = "[熊猫]";
    public static final String ee_55 = "[兔子]";
    public static final String ee_56 = "[心]";
    public static final String ee_57 = "[伤心]";
    public static final String ee_58 = "[猪头]";
    public static final String ee_59 = "[握手]";
    public static final String ee_60 = "[作揖]";
    public static final String ee_61 = "[赞]";
    public static final String ee_62 = "[耶]";
    public static final String ee_63 = "[good]";
    public static final String ee_64 = "[弱]";
    public static final String ee_65 = "[NO]";
    public static final String ee_66 = "[ok]";
    public static final String ee_67 = "[haha]";
    public static final String ee_68 = "[来]";
    public static final String ee_69 = "[拳头]";
    public static final String ee_70 = "[威武]";
    public static final String ee_71 = "[鲜花]";
    public static final String ee_72 = "[钟]";
    public static final String ee_73 = "[浮云]";
    public static final String ee_74 = "[飞机]";
    public static final String ee_75 = "[月亮]";
    public static final String ee_76 = "[太阳]";
    public static final String ee_77 = "[微风]";
    public static final String ee_78 = "[下雨]";
    public static final String ee_79 = "[给力]";
    public static final String ee_80 = "[神马]";
    public static final String ee_81 = "[围观]";
    public static final String ee_82 = "[话筒]";
    public static final String ee_83 = "[奥特曼]";
    public static final String ee_84 = "[草泥马]";
    public static final String ee_85 = "[萌]";
    public static final String ee_86 = "[囧]";
    public static final String ee_87 = "[礼物]";
    public static final String ee_88 = "[喜]";
    public static final String ee_89 = "[围脖]";
    public static final String ee_90 = "[音乐]";
    public static final String ee_91 = "[绿丝带]";
    public static final String ee_92 = "[蛋糕]";
    public static final String ee_93 = "[蜡烛]";
    public static final String ee_94 = "[干杯]";
    public static final String ee_95 = "[男孩儿]";
    public static final String ee_96 = "[女孩儿]";
    public static final String ee_97 = "[肥皂]";
    public static final String ee_98 = "[照相机]";
    public static final String ee_99 = "[沙尘暴]";


    /// 再添加35个表情显示默认环信表情编码 从101-135


	public static final String ee_101 = "[):]";
	public static final String ee_102 = "[:D]";
	public static final String ee_103 = "[;)]";
	public static final String ee_104 = "[:-o]";
	public static final String ee_105 = "[:p]";
	public static final String ee_106 = "[(H)]";
	public static final String ee_107 = "[:@]";
	public static final String ee_108 = "[:s]";
	public static final String ee_109 = "[:$]";
	public static final String ee_110 = "[:(]";
	public static final String ee_111 = "[:'(]";
	public static final String ee_112 = "[:|]";
	public static final String ee_113 = "[(a)]";
	public static final String ee_114 = "[8o|]";
	public static final String ee_115 = "[8-|]";
	public static final String ee_116 = "[+o(]";
	public static final String ee_117 = "[<o)]";
	public static final String ee_118 = "[|-)]";
	public static final String ee_119 = "[*-)]";
	public static final String ee_120 = "[:-#]";
	public static final String ee_121 = "[:-*]";
	public static final String ee_122 = "[^o)]";
	public static final String ee_123 = "[8-)]";
	public static final String ee_124 = "[(|)]";
	public static final String ee_125 = "[(u)]";
	public static final String ee_126 = "[(S)]";
	public static final String ee_127 = "[(*)]";
	public static final String ee_128 = "[(#)]";
	public static final String ee_129 = "[(R)]";
	public static final String ee_130 = "[({)]";
	public static final String ee_131 = "[(})]";
	public static final String ee_132 = "[(k)]";
	public static final String ee_133 = "[(F)]";
	public static final String ee_134 = "[(W)]";
	public static final String ee_135 = "[(D)]";

	private static final Factory spannableFactory = Factory.getInstance();
	
	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {

	    addPattern(emoticons, ee_1, R.drawable.ee_1);
	    addPattern(emoticons, ee_2, R.drawable.ee_2);
	    addPattern(emoticons, ee_3, R.drawable.ee_3);
	    addPattern(emoticons, ee_4, R.drawable.ee_4);
	    addPattern(emoticons, ee_5, R.drawable.ee_5);
	    addPattern(emoticons, ee_6, R.drawable.ee_6);
	    addPattern(emoticons, ee_7, R.drawable.ee_7);
	    addPattern(emoticons, ee_8, R.drawable.ee_8);
	    addPattern(emoticons, ee_9, R.drawable.ee_9);
	    addPattern(emoticons, ee_10, R.drawable.ee_10);
	    addPattern(emoticons, ee_11, R.drawable.ee_11);
	    addPattern(emoticons, ee_12, R.drawable.ee_12);
	    addPattern(emoticons, ee_13, R.drawable.ee_13);
	    addPattern(emoticons, ee_14, R.drawable.ee_14);
	    addPattern(emoticons, ee_15, R.drawable.ee_15);
	    addPattern(emoticons, ee_16, R.drawable.ee_16);
	    addPattern(emoticons, ee_17, R.drawable.ee_17);
	    addPattern(emoticons, ee_18, R.drawable.ee_18);
	    addPattern(emoticons, ee_19, R.drawable.ee_19);
	    addPattern(emoticons, ee_20, R.drawable.ee_20);
	    addPattern(emoticons, ee_21, R.drawable.ee_21);
	    addPattern(emoticons, ee_22, R.drawable.ee_22);
	    addPattern(emoticons, ee_23, R.drawable.ee_23);
	    addPattern(emoticons, ee_24, R.drawable.ee_24);
	    addPattern(emoticons, ee_25, R.drawable.ee_25);
	    addPattern(emoticons, ee_26, R.drawable.ee_26);
	    addPattern(emoticons, ee_27, R.drawable.ee_27);
	    addPattern(emoticons, ee_28, R.drawable.ee_28);
	    addPattern(emoticons, ee_29, R.drawable.ee_29);
	    addPattern(emoticons, ee_30, R.drawable.ee_30);
	    addPattern(emoticons, ee_31, R.drawable.ee_31);
	    addPattern(emoticons, ee_32, R.drawable.ee_32);
	    addPattern(emoticons, ee_33, R.drawable.ee_33);
	    addPattern(emoticons, ee_34, R.drawable.ee_34);
	    addPattern(emoticons, ee_35, R.drawable.ee_35);

	    addPattern(emoticons, ee_36, R.drawable.ee_36);
	    addPattern(emoticons, ee_37, R.drawable.ee_37);
	    addPattern(emoticons, ee_38, R.drawable.ee_38);
	    addPattern(emoticons, ee_39, R.drawable.ee_39);

	    addPattern(emoticons, ee_40, R.drawable.ee_40);
	    addPattern(emoticons, ee_41, R.drawable.ee_41);
	    addPattern(emoticons, ee_42, R.drawable.ee_42);
	    addPattern(emoticons, ee_43, R.drawable.ee_43);
	    addPattern(emoticons, ee_44, R.drawable.ee_44);
	    addPattern(emoticons, ee_45, R.drawable.ee_45);
	    addPattern(emoticons, ee_46, R.drawable.ee_46);
	    addPattern(emoticons, ee_47, R.drawable.ee_47);
	    addPattern(emoticons, ee_48, R.drawable.ee_48);
	    addPattern(emoticons, ee_49, R.drawable.ee_49);

	    addPattern(emoticons, ee_50, R.drawable.ee_50);
	    addPattern(emoticons, ee_51, R.drawable.ee_51);
	    addPattern(emoticons, ee_52, R.drawable.ee_52);
	    addPattern(emoticons, ee_53, R.drawable.ee_53);
	    addPattern(emoticons, ee_54, R.drawable.ee_54);
	    addPattern(emoticons, ee_55, R.drawable.ee_55);
	    addPattern(emoticons, ee_56, R.drawable.ee_56);
	    addPattern(emoticons, ee_57, R.drawable.ee_57);
	    addPattern(emoticons, ee_58, R.drawable.ee_58);
	    addPattern(emoticons, ee_59, R.drawable.ee_59);

	    addPattern(emoticons, ee_60, R.drawable.ee_60);
	    addPattern(emoticons, ee_61, R.drawable.ee_61);
	    addPattern(emoticons, ee_62, R.drawable.ee_62);
	    addPattern(emoticons, ee_63, R.drawable.ee_63);
	    addPattern(emoticons, ee_64, R.drawable.ee_64);
	    addPattern(emoticons, ee_65, R.drawable.ee_65);
	    addPattern(emoticons, ee_66, R.drawable.ee_66);
	    addPattern(emoticons, ee_67, R.drawable.ee_67);
	    addPattern(emoticons, ee_68, R.drawable.ee_68);
	    addPattern(emoticons, ee_69, R.drawable.ee_69);

	    addPattern(emoticons, ee_70, R.drawable.ee_70);
	    addPattern(emoticons, ee_71, R.drawable.ee_71);
	    addPattern(emoticons, ee_72, R.drawable.ee_72);
	    addPattern(emoticons, ee_73, R.drawable.ee_73);
	    addPattern(emoticons, ee_74, R.drawable.ee_74);
	    addPattern(emoticons, ee_75, R.drawable.ee_75);
	    addPattern(emoticons, ee_76, R.drawable.ee_76);
	    addPattern(emoticons, ee_77, R.drawable.ee_77);
	    addPattern(emoticons, ee_78, R.drawable.ee_78);
	    addPattern(emoticons, ee_79, R.drawable.ee_79);

	    addPattern(emoticons, ee_80, R.drawable.ee_80);
	    addPattern(emoticons, ee_81, R.drawable.ee_81);
	    addPattern(emoticons, ee_82, R.drawable.ee_82);
	    addPattern(emoticons, ee_83, R.drawable.ee_83);
	    addPattern(emoticons, ee_84, R.drawable.ee_84);
	    addPattern(emoticons, ee_85, R.drawable.ee_85);
	    addPattern(emoticons, ee_86, R.drawable.ee_86);
	    addPattern(emoticons, ee_87, R.drawable.ee_87);
	    addPattern(emoticons, ee_88, R.drawable.ee_88);
	    addPattern(emoticons, ee_89, R.drawable.ee_89);

	    addPattern(emoticons, ee_90, R.drawable.ee_90);
	    addPattern(emoticons, ee_91, R.drawable.ee_91);
	    addPattern(emoticons, ee_92, R.drawable.ee_92);
	    addPattern(emoticons, ee_93, R.drawable.ee_93);
	    addPattern(emoticons, ee_94, R.drawable.ee_94);
	    addPattern(emoticons, ee_95, R.drawable.ee_95);
	    addPattern(emoticons, ee_96, R.drawable.ee_96);
	    addPattern(emoticons, ee_97, R.drawable.ee_97);
	    addPattern(emoticons, ee_98, R.drawable.ee_98);
	    addPattern(emoticons, ee_99, R.drawable.ee_99);



        /// 再添加35个表情显示默认环信表情编码 从101-135

	    addPattern(emoticons, ee_101, R.drawable.ee_101);
	    addPattern(emoticons, ee_102, R.drawable.ee_102);
	    addPattern(emoticons, ee_103, R.drawable.ee_103);
	    addPattern(emoticons, ee_104, R.drawable.ee_104);
	    addPattern(emoticons, ee_105, R.drawable.ee_105);
	    addPattern(emoticons, ee_106, R.drawable.ee_106);
	    addPattern(emoticons, ee_107, R.drawable.ee_107);
	    addPattern(emoticons, ee_108, R.drawable.ee_108);
	    addPattern(emoticons, ee_109, R.drawable.ee_109);


	    addPattern(emoticons, ee_110, R.drawable.ee_110);
	    addPattern(emoticons, ee_111, R.drawable.ee_111);
	    addPattern(emoticons, ee_112, R.drawable.ee_112);
	    addPattern(emoticons, ee_113, R.drawable.ee_113);
	    addPattern(emoticons, ee_114, R.drawable.ee_114);
	    addPattern(emoticons, ee_115, R.drawable.ee_115);
	    addPattern(emoticons, ee_116, R.drawable.ee_116);
	    addPattern(emoticons, ee_117, R.drawable.ee_117);
	    addPattern(emoticons, ee_118, R.drawable.ee_118);
	    addPattern(emoticons, ee_119, R.drawable.ee_119);
	    addPattern(emoticons, ee_120, R.drawable.ee_120);
	    addPattern(emoticons, ee_121, R.drawable.ee_121);
	    addPattern(emoticons, ee_122, R.drawable.ee_122);
	    addPattern(emoticons, ee_123, R.drawable.ee_123);
	    addPattern(emoticons, ee_124, R.drawable.ee_124);
	    addPattern(emoticons, ee_125, R.drawable.ee_125);
	    addPattern(emoticons, ee_126, R.drawable.ee_126);
	    addPattern(emoticons, ee_127, R.drawable.ee_127);
	    addPattern(emoticons, ee_128, R.drawable.ee_128);
	    addPattern(emoticons, ee_129, R.drawable.ee_129);

	    addPattern(emoticons, ee_130, R.drawable.ee_130);
	    addPattern(emoticons, ee_131, R.drawable.ee_131);
	    addPattern(emoticons, ee_132, R.drawable.ee_132);
	    addPattern(emoticons, ee_133, R.drawable.ee_133);
	    addPattern(emoticons, ee_134, R.drawable.ee_134);
	    addPattern(emoticons, ee_135, R.drawable.ee_135);



	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
	        int resource) {
	    map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
        /// 遍历map 找到对应的表情编码,进行替换
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {

	        Matcher matcher = entry.getKey().matcher(spannable);

	        while (matcher.find()) {

	               boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(entry.getValue());
                    int squarewidth = DensityUtil.dip2px(context, 18);
                    Bitmap bitmap =Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), squarewidth, squarewidth,true);


//                    spannable.setSpan(new CenteredImageSpan(context,entry.getValue()),
//	                        matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spannable.setSpan(new VerticalImageSpan(context,bitmap),
	                        matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                    spannable.setSpan(new ImageSpan(context,bitmap),
//	                        matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//	                spannable.setSpan(new ImageSpan(context, entry.getValue()),
//	                        matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	            }
	        }
	    }
	    return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	public static boolean containsKey(String key){
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(key);
	        if (matcher.find()) {
	        	b = true;
	        	break;
	        }
		}
		
		return b;
	}
	
	
	
}
