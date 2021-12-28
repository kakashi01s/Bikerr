package com.firefly.bikerr.navigation.community.map.bikerr.ui.AboutActivities

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.firefly.bikerr.navigation.community.map.bikerr.R

class AboutActivity : AppCompatActivity() {
    var heading : TextView? = null
    var body : TextView? = null
    var wapp : CardView? = null
    var ig : CardView? = null
    var mail : CardView? = null
    var wapptext : TextView? = null
    var igtext : TextView? = null
    var mailtext : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initViews()
        wapp!!.visibility = View.INVISIBLE
        ig!!.visibility = View.INVISIBLE
        mail!!.visibility = View.INVISIBLE
        wapptext!!.visibility = View.INVISIBLE
        igtext!!.visibility = View.INVISIBLE
        mailtext!!.visibility = View.INVISIBLE

        ig!!.setOnClickListener {
            launchInsta()
        }
        wapp!!.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://api.whatsapp.com/send?phone=+917427008094&text= Hello,Firefly Solutions"
                    )
                )
            )
        }
        mail!!.setOnClickListener {

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                this.data = Uri.parse("mailto:fireflybikerr@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }


        val data = intent.getStringExtra("heading")
        if (data == "about"){
            heading!!.text = data
            body!!.text = "Welcome to Bikerr, your number one source for all things a Biker Needs. We're dedicated to giving you the very best of experience, with a focus on Quality News, Products, and Chat Experience.\n" +
                    "\n" +
                    "\n" +
                    "Founded in 2021 by Nithik Mali, Bikerr has come a long way from its beginnings . We hope you Continue the Support because We Are Just Starting!, We have Big and Great plans Ahead to help the Biking Community. We now serve customers all over India, and are thrilled that we're able to turn our passion into our Community.\n" +
                    "\n" +
                    "\n" +
                    "We hope you enjoy our products/services as much as we enjoy offering them to you. If you have any questions or comments, please don't hesitate to contact us.\n" +
                    "\n" +
                    "\n" +
                    "Sincerely,\n" +
                    "\n" +
                    "Bikerr Team\n" +
            "\n" +"Address : 1400 , New Bhupalpura VrindavanDham No.3 , Udaipur, Rajasthan(313001)"


        }
        else if (data == "privacy"){
            heading!!.text = data
            body!!.text = "By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Nithik Mali.\n" +
                    "Nithik Mali is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\n" +
                    "The Bikerr app stores and processes personal data that you have provided to us, in order to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the Bikerr app won’t work properly or at all.\n" +
                    "The app does use third party services that declare their own Terms and Conditions.\n" +
                    "Search Terms and Conditions of third party service providers used by the app\n" +
                    "* Google Play Services\n" +
                    "* AdMob\n" +
                    "* StreamChat\n" +
                    "You should be aware that there are certain things that Nithik Mali will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but Nithik Mali cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\n" +
                    "\n" +
                    "If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\n" +
                    "Along the same lines, Nithik Mali cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Nithik Mali cannot accept responsibility.\n" +
                    "With respect to Nithik Mali’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Nithik Mali accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\n" +
                    "At some point, we may wish to update the app. The app is currently available on Android – the requirements for system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Nithik Mali does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\n" +
                    "Changes to This Terms and Conditions\n" +
                    "I may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Terms and Conditions on this page.\n" +
                    "These terms and conditions are effective as of 2021-07-30\n" +
                    "Contact Us\n" +
                    "If you have any questions or suggestions about my Terms and Conditions, do not hesitate to contact me at fireflybikerr@gmail.com.\n"
        }
        else if(data == "contactus"){
            heading!!.text = data
            body!!.text = " We are Eager to help You out, Please Reach us at one of the Following Options."
            body!!.setTextColor(R.color.black!!)
            body!!.textSize = 20F
            wapp!!.visibility = View.VISIBLE
            ig!!.visibility = View.VISIBLE
            mail!!.visibility = View.VISIBLE
            wapptext!!.visibility = View.VISIBLE
            igtext!!.visibility = View.VISIBLE
            mailtext!!.visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        heading = findViewById(R.id.headingabout)
        body = findViewById(R.id.bodyabout)
        wapp = findViewById(R.id.whatsapp)
        ig = findViewById(R.id.instagram)
        mail = findViewById(R.id.emaill)
        igtext = findViewById(R.id.instagramtext)
        wapptext = findViewById(R.id.whatsapptext)
        mailtext = findViewById(R.id.emailtext)


    }
    fun launchInsta() {
        val uriForApp: Uri = Uri.parse("https://instagram.com/bikerrapp?utm_medium=copy_link")
        val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

        val uriForBrowser: Uri = Uri.parse("https://instagram.com/bikerrapp?utm_medium=copy_link")
        val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)

        forApp.component =
            ComponentName(
                "com.instagram.android",
                "com.instagram.android.activity.UrlHandlerActivity"
            )

        try {
            startActivity(forApp)
        } catch (e: ActivityNotFoundException) {
            startActivity(forBrowser)
        }
    }

}