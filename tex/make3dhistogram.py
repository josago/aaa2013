import csv, numpy

def make3dhistogram(x, y, z, zmin, output):
    writer = csv.writer(open(output, 'wb'), delimiter=' ')
    i = 0
    for j in range(len(y)):
        writer.writerow((x[i], y[j], zmin))
        writer.writerow((x[i], y[j], zmin))
    for i in range(len(x)-1):        
        writer.writerow((x[i], y[0], zmin))
        for j in range(len(y)-1):
            writer.writerow((x[i], y[j], z[i][j]))
            writer.writerow((x[i], y[j+1], z[i][j]))            
        writer.writerow((x[i], y[len(y)-1], zmin))
        writer.writerow([])
        writer.writerow((x[i+1], y[0], zmin))
        for j in range(len(y)-1):
            writer.writerow((x[i+1], y[j], z[i][j]))
            writer.writerow((x[i+1], y[j+1], z[i][j]))          
        writer.writerow((x[i+1], y[len(y)-1], zmin))
        writer.writerow([])

    i = len(x)-1
    for j in range(len(y)):
        writer.writerow((x[i], y[j], zmin))
        writer.writerow((x[i], y[j], zmin))

x = [0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]
y = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1]
z = numpy.array([
[189.457, 23.086, 22.211, 22.501, 21.012, 21.991, 21.721, 20.359, 21.962, 20.94],
[134.525, 20.825, 20.633, 21.481, 21.485, 21.188, 21.435, 20.788, 21.029, 20.845],
[157.379, 21.803, 21.036, 20.783, 20.602, 20.636, 20.926, 20.816, 20.926, 21.379],
[150.555, 20.873, 21.179, 20.718, 21.169, 21.219, 20.589, 21.24, 20.93, 20.267],
[149.871, 21.158, 20.436, 20.927, 20.942, 21.19, 21.42, 20.927, 20.572, 20.664],
[149.527, 20.476, 20.811, 20.515, 21.047, 21.367, 20.459, 21.384, 20.666, 21.067],
[152.504, 20.909, 20.645, 20.96, 21.094, 21.648, 20.887, 21.404, 21.114, 20.449],
[152.456, 21.059, 22.156, 20.985, 20.683, 20.93, 20.674, 21.598, 21.944, 21.937],
[159.248, 20.997, 22.054, 22.256, 23.061, 21.309, 23.561, 21.529, 23.072, 23.534],
[398.888, 34.242, 23.882, 23.917, 67.421, 22.508, 34.974, 40.936, 35.25, 21.777],
])
make3dhistogram(x, y, z, 0.0, 'test1')