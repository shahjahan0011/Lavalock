# Lavalock

# Randomness and Encryption from Images

## Overview
This project demonstrates how to generate cryptographically secure random numbers by capturing entropy (randomness) from images taken via a webcam. By leveraging real-world data and combining it with cryptographic hashing techniques, the system produces unique random numbers. 

This README will walk you through the project’s structure and  key concepts. 

---

## Features
- **Live Entropy Source**: Uses a live webcam feed to capture raw randomness from real-world images.
- **Cryptographic Hashing**: Utilizes the SHA-256 algorithm to transform image data into a secure hash.
- **Hexadecimal Seed Conversion**: Converts hashes into numeric seeds for generating random numbers.
- **Dynamic Randomness**: Even the smallest changes in the captured image result in entirely different random outputs.
- **Modular Design**: Each step in the process is independent, ensuring clean and maintainable code.

---

## How It Works

### 1. Capturing Entropy
The system continuously captures frames from a webcam using OpenCV. Each frame represents a real-world snapshot, full of unpredictable patterns and variations.

### 2. Extracting Pixel Data
The captured frame is processed to extract pixel data, transforming the image into a byte array. Each byte represents specific colors and brightness levels in the image.

### 3. Hashing with SHA-256
The pixel data is fed into the SHA-256 cryptographic hashing algorithm. This process generates a unique 256-bit hash for every image. Even minor changes in the image—like a finger covering the webcam—result in dramatically different hashes.

### 4. Hexadecimal Conversion
The hash is converted into a hexadecimal string, a human-readable format that serves as the seed for the random number generator.

### 5. Random Number Generation
The hexadecimal hash is transformed into a numeric seed. This seed powers the random number generator, producing numbers that are as chaotic as the input image.

---

## Use Cases
- **Enhanced Security**: Secure random number generation for cryptographic applications.
- **Gaming Systems**: Randomness for game mechanics or procedural generation.
- **Simulation and Modeling**: Creating unpredictable scenarios for simulations.
  
## Core Method: `calculateImageHash`
### Steps:
1. **Extract Pixel Data**: Captures a frame and extracts its pixel values as a byte array.
2. **Hashing**: Processes the byte array through the SHA-256 algorithm to create a unique hash.
3. **Hexadecimal Conversion**: Converts the hash into a hexadecimal string.
4. **Seed Generation**: Uses the hash as a seed for generating random numbers.

### Code Snippet:
```python
def calculate_image_hash(frame):
    import hashlib
    import numpy as np

    # Convert frame to a byte array
    pixel_data = frame.tobytes()

    # Generate SHA-256 hash
    hash_object = hashlib.sha256(pixel_data)
    hash_hex = hash_object.hexdigest()

    return hash_hex
```

---

## Future Enhancements
- **Additional Entropy Sources**: Combine webcam data with other randomness sources (e.g., microphone noise).
- **Improved UI**: Create a more interactive graphical interface for the application.
- **Distributed Randomness**: Generate random numbers collaboratively from multiple webcams.

## Contributions
Contributions are welcome! Feel free to fork the repository and submit a pull request with your improvements.

---

## Contact
If you have questions or feedback, please contact:
- **Name**: Jahan Shah
- **Email**: talk2jhere@gmail.com
