package com.callname.project;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
public class VoiceUtils {
    /**
     * 
     * @Title: strat @Description: �÷�������Ҫ���ã��ʶ� @param @param content @param @param
     * type �趨�ļ� 0:��ʼ��1ֹͣ @return �������ͣ�void @throws
     */
    public static void strat(String content, int type) {
        //���Sapi.SpVoice����Ҫ��װʲô�����𣬸о�ƽ���޹ʾ�����
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        // Dispatch����ʲô�ģ�
        Dispatch sapo = sap.getObject();
        if (type == 0) {
            try {
                // ���� 0-100
                sap.setProperty("Volume", new Variant(100));
                // �����ʶ��ٶ� -10 �� +10
                sap.setProperty("Rate", new Variant(1.0));
                Variant defalutVoice = sap.getProperty("Voice");
                Dispatch dispdefaultVoice = defalutVoice.toDispatch();
                Variant allVoices = Dispatch.call(sapo, "GetVoices");
                Dispatch dispVoices = allVoices.toDispatch();
                Dispatch setvoice = Dispatch.call(dispVoices, "Item", new Variant(1)).toDispatch();
                ActiveXComponent voiceActivex = new ActiveXComponent(dispdefaultVoice);
                ActiveXComponent setvoiceActivex = new ActiveXComponent(setvoice);
                Variant item = Dispatch.call(setvoiceActivex, "GetDescription");
                // ִ���ʶ�
                Dispatch.call(sapo, "Speak", new Variant(content));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sapo.safeRelease();
                sap.safeRelease();
            }
        } else {
            // ֹͣ
            try {
                Dispatch.call(sapo, "Speak", new Variant(content), new Variant(2));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
