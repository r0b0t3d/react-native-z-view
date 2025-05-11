import { View, StyleSheet } from 'react-native';
import { ZView } from 'react-native-z-view-upgrade';

export default function App() {
  return (
    <View style={styles.container}>
      <ZView left={10} top={10} bottom={10} right={10}>
        <View style={styles.box} />
      </ZView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'red',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
    backgroundColor: 'blue',
  },
});
