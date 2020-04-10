package mn.mta.pos.exam;

public class BridgePosAPI {
    static{
        String os = System.getProperty("os.name");
        if (os.toUpperCase().contains("WINDOWS")) {
            System.loadLibrary("icudt53");
            System.loadLibrary("icuuc53");
            System.loadLibrary("icuin53");
            System.loadLibrary("Qt5Core");
            System.loadLibrary("Qt5SQL");
            System.loadLibrary("Qt5Network");
            System.loadLibrary("PosAPI");
        }
        System.loadLibrary("BridgePosAPI");
    }

    /**
     * Хэрэглэгчийн системээс борлуулалтын мэдээллийг хүлээн авч буцаан баримтын ДДТД, сугалааны
     * дугаар, баримт хэвлсэн огноо, баримтын код, QrCode гэсэн утгуудыг буцаана.
     */
    public static native String put(String data);

    /**
     * Хэрэглэгчийн системээс бүртгэгдэн гарсан баримтийг хүчингүй болгоно. Уг функцээр буцаасан
     * нөхцөлд тухайн баримтан дээрх сугалаа болон дахин давтагдашгүй төлбөрийн дугаар/ДДТД/ зэргийг
     * хүчингүй гэж үзнэ.
     */
    public static native String returnBill(String data);

    /**
     * Баримтын мэдээллийг хуулинд заасны дагуу борлуулалт гэж үзэн бүртгэсэнээс хойш 72 цагийн
     * дотор илгээх ёстой. Уг функц нь борлуулалтын мэдээллийг татварын нэгдсэн системд илгээнэ.
     *
     * Мөн хэрэглэгчийн системд PosAPI-г анх удаа суурьлуулж буй эсвэл сугалааны багц дууссан нөхцөлд
     * мөн уг функцийг ажиллуулж тохиргоо болон шинэ сугалааны багцийг татаж авна.
     */
    public static native String sendData();

    /**
     * Хэрэглэгчийн системийн тогтвортой ажиллагааг хангах шаардлагын улмаас PosAPI сангийн
     * ажиллагааг шалгана. Хэрэглэгчийн системийг ажиллуулж буй үйлдлийн системийн хэрэглэгч нь заавал
     * өөрийн HOME directory-той байх ёстой. Хэрэв уг шаардлагыг хангаагүй бол уг функц нь амжилтгүй гэсэн
     * утгыг буцаана.
     */
    public static native String checkAPI();

    /**
     * Хэрэглэгчийн систем нь нэгээс олон PosAPI ашиглаж буй үед харьцаж буй PosAPI-гийн мэдээллийг
     * харах шаардлага тулгардаг. Уг функц нь уг асуудлыг шийдэж буй бөгөөд уг функцийн тусламжтайгаар
     * хэрэглэгчийн систем нь тухайн ашиглаж буй PosAPI-гийн дотоод мэдээллүүдийг авна.
     */
    public static native String getInformation();

    /**
     * PosAPI нь цаашид шинээр нэмэлт функцүүд нэмэгдэх бөгөөд нэмэгдсэн функцийг ашиглахын тулд
     * заавал өөрийн PosAPI-г шинээр татах шаардлаггүй юм. Уг нэмэлт функцүүдийг уг функцийн тусламжтайгаар дуудана.
     */
    public static native String callFunction(String funcName, String data);
}
