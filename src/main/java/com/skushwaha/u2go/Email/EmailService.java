package com.skushwaha.u2go.Email;

import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final Resend resend;

    @Value("${resend.from}")
    private String from;

    /**
     * Sends the short URL to the user's email after shortening.
     */
    @Async("emailTaskExecutor")
    public void sendShortUrlEmail(
            String email,
            String originalUrl,
            String shortUrl,
            String qrCodeUrl
    ) {

        String html = """
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Your Short URL - U2Go</title>
        </head>

        <body style="
            margin:0;
            padding:0;
            background-color:#F6F8FD;
            font-family:Arial, Helvetica, sans-serif;
            color:#0B1A2F;
        ">

        <!-- Background -->
        <table
            role="presentation"
            width="100%"
            cellspacing="0"
            cellpadding="0"
            border="0"
            style="
                width:100%;
                background-color:#F6F8FD;
            "
        >
            <tr>
                <td
                    align="center"
                    style="padding:35px 15px;"
                >

                    <!-- Main Card -->
                    <table
                        role="presentation"
                        width="600"
                        cellspacing="0"
                        cellpadding="0"
                        border="0"
                        style="
                            width:100%;
                            max-width:600px;
                            background-color:#FFFFFF;
                            border-radius:18px;
                            overflow:hidden;
                            box-shadow:0 12px 40px rgba(20,50,150,0.08);
                        "
                    >

                        <!-- Top gradient bar -->
                        <tr>
                            <td
                                style="
                                    height:5px;
                                    background:linear-gradient(135deg,#1532B0,#2B5BFF,#7645F0);
                                    font-size:0;
                                    line-height:0;
                                "
                            >
                                &nbsp;
                            </td>
                        </tr>


                        <!-- Brand -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:32px 30px 5px 30px;
                                "
                            >

                                <table
                                    role="presentation"
                                    cellspacing="0"
                                    cellpadding="0"
                                    border="0"
                                >
                                    <tr>

                                        <td
                                            align="center"
                                            valign="middle"
                                            width="46"
                                            height="46"
                                            style="
                                                width:46px;
                                                height:46px;
                                                background:linear-gradient(135deg,#1532B0,#7645F0);
                                                border-radius:12px;
                                                color:#FFFFFF;
                                                font-size:24px;
                                                font-weight:bold;
                                                text-align:center;
                                                line-height:46px;
                                            "
                                        >
                                            🔗
                                        </td>

                                        <td
                                            width="12"
                                            style="
                                                width:12px;
                                                font-size:0;
                                                line-height:0;
                                            "
                                        >
                                            &nbsp;
                                        </td>

                                        <td
                                            style="
                                                font-size:26px;
                                                font-weight:bold;
                                                color:#1532B0;
                                                vertical-align:middle;
                                                letter-spacing:-0.5px;
                                            "
                                        >
                                            U2<span style="color:#7645F0;">Go</span>
                                        </td>

                                    </tr>
                                </table>

                            </td>
                        </tr>


                        <!-- Title -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:20px 35px 5px 35px;
                                "
                            >

                                <h1
                                    style="
                                        margin:0;
                                        padding:0;
                                        font-size:28px;
                                        line-height:36px;
                                        font-weight:700;
                                        color:#050D1C;
                                    "
                                >
                                    Your Short Link is Ready 🎉
                                </h1>

                            </td>
                        </tr>


                        <!-- Description -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:8px 45px 25px 45px;
                                "
                            >

                                <p
                                    style="
                                        margin:0;
                                        padding:0;
                                        font-size:15px;
                                        line-height:24px;
                                        color:#47567A;
                                    "
                                >
                                    Here's your shortened URL. It never expires
                                    and works forever.
                                </p>

                            </td>
                        </tr>


                        <!-- Short URL Box -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:0 35px;
                                "
                            >

                                <table
                                    role="presentation"
                                    width="100%"
                                    cellspacing="0"
                                    cellpadding="0"
                                    border="0"
                                    style="
                                        width:100%;
                                        max-width:460px;
                                        background-color:#F0F5FF;
                                        border:1px solid #C7D9FF;
                                        border-radius:14px;
                                    "
                                >

                                    <tr>
                                        <td
                                            align="center"
                                            style="
                                                padding:20px 15px 8px 15px;
                                            "
                                        >

                                            <p
                                                style="
                                                    margin:0;
                                                    padding:0;
                                                    font-size:11px;
                                                    line-height:18px;
                                                    font-weight:bold;
                                                    letter-spacing:1.5px;
                                                    text-transform:uppercase;
                                                    color:#7A8BAB;
                                                "
                                            >
                                                YOUR SHORT URL
                                            </p>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td
                                            align="center"
                                            style="
                                                padding:0 15px 20px 15px;
                                            "
                                        >

                                            <a
                                                href="{{SHORT_URL}}"
                                                style="
                                                    margin:0;
                                                    padding:0;
                                                    font-family:Arial, Helvetica, sans-serif;
                                                    font-size:20px;
                                                    line-height:28px;
                                                    font-weight:bold;
                                                    color:#2B5BFF;
                                                    text-decoration:none;
                                                    word-break:break-all;
                                                    letter-spacing:-0.3px;
                                                "
                                            >
                                                {{SHORT_URL}}
                                            </a>

                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>


                        <!-- CTA Button -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:26px 35px 10px 35px;
                                "
                            >

                                <a
                                    href="{{SHORT_URL}}"
                                    style="
                                        display:inline-block;
                                        padding:15px 38px;
                                        background:linear-gradient(135deg,#1532B0,#2B5BFF,#7645F0);
                                        color:#FFFFFF;
                                        text-decoration:none;
                                        font-size:15px;
                                        font-weight:bold;
                                        border-radius:12px;
                                        letter-spacing:0.3px;
                                    "
                                >
                                    Open Short Link &rarr;
                                </a>

                            </td>
                        </tr>

                        <!-- Original URL -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:30px 45px 10px 45px;
                                "
                            >

                                <p
                                    style="
                                        margin:0 0 8px 0;
                                        padding:0;
                                        font-size:11px;
                                        line-height:18px;
                                        font-weight:bold;
                                        letter-spacing:1.2px;
                                        text-transform:uppercase;
                                        color:#7A8BAB;
                                        text-align:left;
                                    "
                                >
                                    ORIGINAL URL
                                </p>

                                <table
                                    role="presentation"
                                    width="100%"
                                    cellspacing="0"
                                    cellpadding="0"
                                    border="0"
                                    style="
                                        width:100%;
                                        background-color:#F6F8FD;
                                        border:1px solid #E4EBF8;
                                        border-radius:10px;
                                    "
                                >
                                    <tr>
                                        <td
                                            style="
                                                padding:14px 16px;
                                                font-size:13px;
                                                line-height:20px;
                                                color:#47567A;
                                                word-break:break-all;
                                                text-align:left;
                                            "
                                        >
                                            {{ORIGINAL_URL}}
                                        </td>
                                    </tr>
                                </table>

                            </td>
                        </tr>


                        <!-- Divider -->
                        <tr>
                            <td
                                style="
                                    padding:28px 45px 0 45px;
                                "
                            >

                                <table
                                    role="presentation"
                                    width="100%"
                                    cellspacing="0"
                                    cellpadding="0"
                                    border="0"
                                >
                                    <tr>
                                        <td
                                            style="
                                                height:1px;
                                                background-color:#E4EBF8;
                                                font-size:0;
                                                line-height:0;
                                            "
                                        >
                                            &nbsp;
                                        </td>
                                    </tr>
                                </table>

                            </td>
                        </tr>


                        <!-- Feature highlights -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:22px 35px 10px 35px;
                                "
                            >

                                <table
                                    role="presentation"
                                    width="100%"
                                    cellspacing="0"
                                    cellpadding="0"
                                    border="0"
                                >
                                    <tr>

                                        <td
                                            align="center"
                                            width="33%"
                                            style="
                                                padding:10px 6px;
                                            "
                                        >
                                            <div style="
                                                font-size:20px;
                                                line-height:1;
                                                margin-bottom:6px;
                                            ">♾️</div>
                                            <div style="
                                                font-size:11px;
                                                color:#7A8BAB;
                                                font-weight:bold;
                                                letter-spacing:0.3px;
                                            ">Never Expires</div>
                                        </td>

                                        <td
                                            align="center"
                                            width="33%"
                                            style="
                                                padding:10px 6px;
                                            "
                                        >
                                            <div style="
                                                font-size:20px;
                                                line-height:1;
                                                margin-bottom:6px;
                                            ">🎨</div>
                                            <div style="
                                                font-size:11px;
                                                color:#7A8BAB;
                                                font-weight:bold;
                                                letter-spacing:0.3px;
                                            ">900 QR Styles</div>
                                        </td>

                                        <td
                                            align="center"
                                            width="33%"
                                            style="
                                                padding:10px 6px;
                                            "
                                        >
                                            <div style="
                                                font-size:20px;
                                                line-height:1;
                                                margin-bottom:6px;
                                            ">📊</div>
                                            <div style="
                                                font-size:11px;
                                                color:#7A8BAB;
                                                font-weight:bold;
                                                letter-spacing:0.3px;
                                            ">Click Tracking</div>
                                        </td>

                                    </tr>
                                </table>

                            </td>
                        </tr>


                        <!-- Footer -->
                        <tr>
                            <td
                                align="center"
                                style="
                                    padding:22px 30px;
                                    background-color:#F6F8FD;
                                    border-top:1px solid #E4EBF8;
                                "
                            >

                                <p
                                    style="
                                        margin:0;
                                        padding:0;
                                        font-size:15px;
                                        line-height:22px;
                                        font-weight:bold;
                                        color:#1532B0;
                                    "
                                >
                                    U2<span style="color:#7645F0;">Go</span>
                                </p>

                                <p
                                    style="
                                        margin:4px 0 0 0;
                                        padding:0;
                                        font-size:12px;
                                        line-height:18px;
                                        color:#7A8BAB;
                                    "
                                >
                                    Short URLs. Beautiful QR codes.
                                </p>

                                <p
                                    style="
                                        margin:12px 0 0 0;
                                        padding:0;
                                        font-size:11px;
                                        line-height:17px;
                                        color:#A8B3B8;
                                    "
                                >
                                    © 2026 U2Go. All rights reserved.
                                </p>

                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

        </body>
        </html>
        """
                .replace("{{SHORT_URL}}", shortUrl)
                .replace("{{ORIGINAL_URL}}", originalUrl);
                //.replace("{{QR_CODE_URL}}", qrCodeUrl);

        sendEmail(
                email,
                "🎉 Your U2Go short link is ready!",
                html
        );
    }


    /**
     * Sends the email via Resend API.
     */
    private void sendEmail(String to, String subject, String html) {
        try {
            com.resend.services.emails.model.CreateEmailOptions params =
                    com.resend.services.emails.model.CreateEmailOptions.builder()
                            .from(from)
                            .to(to)
                            .subject(subject)
                            .html(html)
                            .build();

            com.resend.services.emails.model.CreateEmailResponse response =
                    resend.emails().send(params);

            System.out.println("Email sent successfully: " + response.getId());

        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
