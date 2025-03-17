from flask import Flask,request,jsonify
from transformers import pipeline

app = Flask(__name__)
sentiment_analyser = pipeline("sentiment-analysis")

def analyze_sentiment(text):
    result = sentiment_analyser(text)

    sentiment_label = result[0]['label']
    confidence_score = result[0]['score']

    score = calculate_score(sentiment_label, confidence_score)

    return jsonify({"score":score}), 200

def calculate_score(label,confidence):

    confidence = confidence*100;
    score = 0

    if label == "POSITIVE":
        if confidence > 80:
            score += 40
        elif confidence > 60:
            score += 20
    elif label == "NEGATIVE":
        if confidence > 80:
            score -= 40
        elif confidence > 60:
            score -= 20

    return score

       

@app.route("/sentiment-analysis", methods=["POST"])
def analyse_text_sentiment():
    data = request.json
    text = data.get("text","")

    if not text:
        return jsonify({"error": "Missing text"}), 400

    return analyze_sentiment(text)
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5003, debug=True)

