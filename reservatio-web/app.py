from flask import Flask, render_template, request, redirect, session, url_for, jsonify
import pyqrcode
import pyrebase
from pyqrcode import QRCode

firebaseConfig = {'apiKey': "AIzaSyBeJoc5lBqb9J_XQ1Asspyvu3PrV9T7_rE",
                  'authDomain': "reservatio-73d49.firebaseapp.com",
                  'projectId': "reservatio-73d49",
                  'storageBucket': "reservatio-73d49.appspot.com",
                  'messagingSenderId': "959466070778",
                  'appId': "1:959466070778:android:25af81ed817bfc99acdf43",
                  'measurementId': "G-ZNK0DXHYEJ",
                  'databaseURL': "https://reservatio-73d49-default-rtdb.asia-southeast1.firebasedatabase.app/"
                  }
firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database()

app = Flask(__name__)
app.config['SECRET_KEY'] = "c6e803cd18a8c528c161eb9fcf013245248506ffb540ff70"


@app.route("/", methods=["GET", "POST"])
def first_page():
    return render_template("index.html")


@app.route("/input/", methods=["GET", "POST"])
def input_page():
    if request.method == "POST":
        s = str(request.form["name"])
        if s == "":
            return render_template("input_page.html", error="Input field cannot be empty !!")
        url = pyqrcode.create(s)
        image_as_str = url.png_as_base64_str(scale=5)
        session['logged_in'] = True
        session['name'] = s
        session['qrcode'] = image_as_str
        d = {s: {"dummy": -2}}
        db.update(d)
        return render_template("service.html", url=image_as_str)
    else:
        if session.get('logged_in') is None:
            return render_template("input_page.html")
        return render_template("service.html", url=session['qrcode'])


@app.route("/user/", methods=["POST", "GET"])
def user():
    if session.get('logged_in') is None:
        return render_template("input_page.html")
    return render_template("service.html", url=session['qrcode'])


@app.route("/contact/", methods=['POST', 'GET'])
def contact():
    return render_template("contact.html")


@app.route("/getdata", methods=['POST', 'GET'])
def getdata():
    data_file = db.child(session['name']).get()
    count = 0
    for details in data_file.each():
        if (details.val() >= 1):
            count = count + 1
    if (count >= 1):
        disp = str(count)
    else:
        disp = "No Customers"
    return jsonify(result=disp)


@app.route("/get_current_user", methods=['POST', 'GET'])
def getuser():
    data_file = db.child(session['name']).get()
    for details in data_file.each():
        if (details.val() == 1):
            return jsonify(result="Next User : " + details.key().split("-")[0])
    return jsonify(result="")


@app.route("/getdata2", methods=['POST', 'GET'])
def getdata2():
    data_file = db.child(session['name']).get()
    d = dict()
    for details in data_file.each():
        if(details.val() == 0):
            db.child(session['name']).child(details.key()).remove()
        elif (details.key() != "dummy"):
            d[details.key()] = details.val()-1
            if d[details.key()] == 0:
                db.child(session['name']).update(d)
                db.child(session['name']).child(details.key()).remove()
                d.pop(details.key())
    db.child(session['name']).update(d)
    return redirect(url_for('user'))


if __name__ == "__main__":
    app.run()
