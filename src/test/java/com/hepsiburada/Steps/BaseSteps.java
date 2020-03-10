package com.hepsiburada.Steps;

import com.hepsiburada.Tests.BaseTest;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;
import webAutomation.helper.ElementHelper;
import webAutomation.helper.StoreHelper;
import webAutomation.model.ElementInfo;


import java.io.*;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class BaseSteps extends BaseTest {


    Logger logger = LoggerFactory.getLogger(getClass());

    Actions actions = new Actions(driver);

    public WebElement findElement(String key) {

        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 30, 100);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;

    }

    public List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait wait = new WebDriverWait(driver, 60);
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(infoParam));
        return elements;
    }

    public void clickElement(WebElement element) {
       element.click();


    }

    public void sendKeys(String key, String text) {

        WebElement element = findElement(key);
        element.sendKeys(text);
    }

    public void hoverElement(String key) {
        WebElement element = findElement(key);
        actions.moveToElement(element).build().perform();
    }

    public void hoverElementWithElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }



    public void randomClick(String key) throws InterruptedException {
        WebElement element = findElement(key);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        Thread.sleep(3000);
        actions.moveToElement(elements.get(index)).build().perform();
        clickElement(elements.get(index));




    }



    public void rastgeleMenu(String key, String key2) throws InterruptedException {
        List<WebElement>elements=findElements(key);
        Random random=new Random();
        int index=random.nextInt(elements.size());

        if(index==0)
        {
            hoverElementWithElement(elements.get(0));
            Thread.sleep(10000);
            List<WebElement>elements1=driver.findElements(By.cssSelector("#elektronik > div > div > div > div.col.lg-3.col-md-3.col-sm-3.menus > ul > li > a"));
            System.out.println(elements1.size());
            int index2=random.nextInt(elements1.size());
            clickElement(elements1.get(index2));

        }
        else {
            elements.get(index).click();
            Thread.sleep(10000);
            List<WebElement>elements2=findElements(key2);
            int index3=random.nextInt(elements2.size());
            elements2.get(index3).click();
        }

    }


    public void loginKontrolEt()
    {
        WebElement element=driver.findElement(By.linkText("Hesabım"));
        try{
            if(element.isDisplayed())
            {
                logger.info("Giris doğrulandı");
            }
        }
        catch (Exception ex)
        {
            logger.info("Giris Yapılmadı");
            throw ex;
        }
    }
    public void sepetKontrolEt(String key)
    {
        WebElement element=findElement(key);
        if(element.isDisplayed())
        {
            logger.info("Sepet boş");
        }
    } public void cvsUrunKaydet(String urunFiyati, String urunAdi, String dosyaAdi) throws InterruptedException, IOException {
        FileWriter csvWriter = new FileWriter(dosyaAdi);
        csvWriter.append("\"" + urunAdi + "\"");
        csvWriter.append(",");
        csvWriter.append("\"" + urunFiyati + "\"");
        csvWriter.flush();
        csvWriter.close();
    }
    public void productPriceWrite(String key,String key2, String csv) throws IOException, InterruptedException {
        String urunAdi= findElement(key).getText();
        String urunFiyati=findElement(key2).getText();
        cvsUrunKaydet(urunAdi, urunFiyati, csv);

        FileWriter csvWriter = new FileWriter("new.csv");
        csvWriter.append(urunAdi);
        csvWriter.append(",");
        csvWriter.append(urunFiyati);
        csvWriter.flush();
        csvWriter.close();
    }
    public String cvsVeriOkuma(String dosyaAdi) {

        BufferedReader br = null;
        String[] kelime = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaAdi), "UTF8"));
            String okunanveri = br.readLine();
            kelime = okunanveri.split("\"");
            System.out.println(kelime);
            logger.info(String.valueOf(kelime));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
        return String.valueOf(kelime[1]);
    }
    public void urunFiyatDogrulamasi(String key, String dosyaAdi) {
        WebElement element = findElement(key);
        try {
            Assert.assertEquals(element.getText(), cvsVeriOkuma(dosyaAdi));
            logger.info("urunun fiyatı doğru");
        } catch (Exception ex) {
            logger.info("urun fiyatı yanlış");
            throw ex;
        }
    }
    public void rastgeleUrunSecme(String key) throws InterruptedException {
        List<WebElement>elements=findElements(key);
        Random random=new Random();
        try {
            int index=random.nextInt(elements.size());
            hoverElementWithElement(elements.get(index));
            Thread.sleep(5000);
            logger.info(key+" elementi üzerine gidildi");
        }
        catch (Exception ex)
        {
            logger.info(key+ " elementi üzerine gidilmedi");
        }

    }
    public void elementTextKarsilastir(String key1, String key2) {
        WebElement element = findElement(key1);
        WebElement element1 = findElement(key2);
        Assert.assertEquals(element.getAttribute("value"), element1.getText());


    }
    public void csvUrunKargoYazdır(String urunToplamDegeri, String urunKargoTutari, String dosyaAdi) throws InterruptedException, IOException {
        FileWriter csvWriter = new FileWriter(dosyaAdi);
        csvWriter.append("\"" + urunToplamDegeri + "\"");
        csvWriter.append(",");
        csvWriter.append("\"" + urunKargoTutari + "\"");
        csvWriter.flush();
        csvWriter.close();
    }


    @Step("<key> saniye bekle")
    public void saniyeBekle(int saniye) throws InterruptedException {


        Thread.sleep(saniye * 1000);
    }

    @Step("<key> elementine tıkla")
    public void clickToElement(String key) {
        try {
            WebElement element = findElement(key);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
            clickElement(element);
            logger.info(key + " elementine tıklandı");
        } catch (Exception ex) {
            logger.info(key + " elementine tıklanmadı");
        }
    }




    @Step("<key> elementine <text> değerini gonder")
    public void sendKeysToElement(String key, String text) {

        sendKeys(key, text);
    }

    @Step("<key> elementine yonel")
    public void hoverToElement(String key) {
        hoverElement(key);
    }


    @Step("<key> elementine rastgele tıkla")
    public void randomCategori(String key) throws InterruptedException {

        randomClick(key);
        // hoverElementWithElement(randomSec(key));
    }

    @Step("Login kontrol yap")
    public void loginkontrol() {
        loginKontrolEt();
    }

    @Step("Sepet kontrol yap")
    public void sepetKontrol() {
        sepetKontrolEt("sepetKontrol");
    }

    @Step("<key> elementini kontrol et eğer varsa tıkla")
    public void elementiKontrolEt(String key) {
        try {
            while (true) {
                WebElement element = findElement(key);
                if (element.isDisplayed()) {
                    element.click();
                    logger.info(key + "elementine tıklandı");
                    driver.navigate().refresh();
                    Thread.sleep(5000);
                } else {
                    break;
                }

            }
        } catch (Exception ex) {
            logger.info(key + " elementi yok");
        }


    }


    @Step("<url> git")
    public void urlGit(String key) {
        driver.get(key);
    }

    @Step("<key> menusunden rastgele bir <kategori> sec")
    public void rastgeleKategori(String key1, String key2) throws InterruptedException {

        rastgeleMenu(key1, key2);
    }


    @Step("<secilenUrun> sec <urunAdi> ve <urunFiyati> nı <csv> dosyasına kaydet")
    public void csvKaydet(String secilenUrun ,String urunAdi, String urunFiyati,String csv) throws IOException, InterruptedException {
        List<WebElement>elements=findElements(secilenUrun);
        Random random=new Random();
        int index=random.nextInt(elements.size());
        try {

            hoverElementWithElement(elements.get(index));
            Thread.sleep(5000);
            logger.info(secilenUrun+" elementi üzerine gidildi");
        }
        catch (Exception ex)
        {
            logger.info(secilenUrun+ " elementi üzerine gidilmedi");
        }
        logger.info("rastgele ürün seçildi");
        Thread.sleep(5000);
        WebElement urunadi = findElement(urunAdi);
        WebElement urunfiyat = findElement(urunFiyati);
        cvsUrunKaydet(urunadi.getText(), urunfiyat.getText(), csv);
        Thread.sleep(5000);
        elements.get(index).click();

    }


    @Step("<key> elementinin textini <csv> dosyasından oku")

    public void csvRead(String key,String dosyaAdi)
    {
        urunFiyatDogrulamasi(key,dosyaAdi);
    }

    @Step("deneme <urun>")
    public void denemeuRUN(String key) throws InterruptedException {
        rastgeleUrunSecme(key);
    }
    @Step("<Eklenen Urun Sayısı> ile <Sepet Uzerindeki Sayı> kontrol et" )
    public void urunsayısıKontrol(String eklenenUrun, String sepettekiSayi) throws InterruptedException {
        elementTextKarsilastir(eklenenUrun, sepettekiSayi);


    }
    @Step("<Urun tutar> ile <Odenecek tutarı> karşılaştır")
    public void tutarKontrol(String urunTutar, String odenecekTutar) throws InterruptedException {
        WebElement element = findElement(urunTutar);
        WebElement element1 = findElement(odenecekTutar);
        elementTextKarsilastir(element.getText(), element1.getText());

    }

    @Step("<urunToplamDegeri> ve <urunKargoTutari> nı <csv> dosyasına yazdır")
    public void csvUrunKargo(String urunToplamDegeri, String urunKargoTutari,String csv) throws IOException, InterruptedException {


        WebElement urunToplamDeger = findElement(urunToplamDegeri);
        WebElement kargoTutar = findElement(urunKargoTutari);
        csvUrunKargoYazdır(urunToplamDeger.getText(), kargoTutar.getText(), csv);


    }
}
