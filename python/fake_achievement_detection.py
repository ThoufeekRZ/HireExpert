from flask import Flask, request, jsonify
from transformers import pipeline

app = Flask(__name__)

# Load zero-shot classifier
classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

# Define categories
labels = ["Realistic", "Slightly Unrealistic", "slightly unbelievable","unbelievable"]


@app.route("/analyze-achievement", methods=["POST"])
def analyze_achievement():
    data = request.json
    achievements = data.get("achievements", [])

    if not achievements:
        return jsonify({"error": "No achievements provided"}), 400

    results = []
    score = 0;
    for achievement in achievements:
        result = classifier(achievement, candidate_labels=labels)
        confidence = round(result["scores"][0], 2)
        print(confidence)
        print(result["labels"][0])
        
        if result["labels"][0] == "slightly unbelievable":
            if confidence > 0.8:
             score -= 7
            elif confidence > 0.6:
             score -= 5
            else:
             score -= 3
        elif result["labels"][0] == "unbelievable":
           if confidence > 0.8:
              score -= 15
           else:
             score -= 10

            
        # results.append({
        #     "achievement": achievement,
        #     "classification": result["labels"][0],  # Highest confidence label
        #     "confidence": round(result["scores"][0], 2)  # Round confidence score
        # })



    return jsonify({"score":(score)}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
